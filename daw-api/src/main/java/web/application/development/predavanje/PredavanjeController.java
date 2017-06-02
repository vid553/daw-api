package web.application.development.predavanje;

import static web.application.development.predavanje.PredavanjeComparator.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian_daschner.siren4javaee.Entity;
import com.sebastian_daschner.siren4javaee.EntityReader;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.exception.Error;
import web.application.development.exception.ErrorLog;
import web.application.development.formatter.Formatter;
import web.application.development.headers.Headers;
import web.application.development.team.Team;
import web.application.development.team.TeamService;

@RestController
public class PredavanjeController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private PredavanjeService predavanjeService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private Formatter formatter;
	
	private HttpHeaders sirenHeader = Headers.SirenHeader();
	private HttpHeaders problemHeader = Headers.ProblemHeader();
	
	//works, if non-existing class -> returns 404
	@RequestMapping(value="/classes", method=RequestMethod.GET) //maps URL /predmeti to method getAllPredmeti
	public ResponseEntity<?> getAllPredmeti() {
		List<Predavanje> predavanja = predavanjeService.getAllPredavanje();
		if (predavanja.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			try {
				JsonObject object = formatter.ReturnJSON(predavanja, new Predavanje());
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String timeStamp = new ErrorLog().WriteErorLog(ex);
		        Error error = new Error("http://localhost:8080/error/server", "Internal server error", "Error ID: " + timeStamp);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	//works if class exists, if non-existing class -> returns 404
	@RequestMapping(value="/classes/{id}", method=RequestMethod.GET)
	public HttpEntity<?> getPredavanje(@PathVariable String id) {
		Predavanje predmet = predavanjeService.getPredavanje(id);
		if (predmet != null) {
			try {
				JsonObject object = formatter.ReturnJSON(predmet);
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String timeStamp = new ErrorLog().WriteErorLog(ex);
		        Error error = new Error("http://localhost:8080/error/server", "Internal server error", "Error ID: " + timeStamp);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	//doesnt work if uri contains special characters
	@RequestMapping(value="/classes", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addPredavanje(@RequestBody Predavanje predmet) { //@RequestBody tells spring that the request pay load is going to contain a user
		predavanjeService.addPredavanje(predmet);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/*example of pay load
	 * {
		"id": "java_class",
		"identifier": "J",
		"enrolment": "true"
		}
	 */
	
	//works
	@RequestMapping(value="/classes/{id}", method=RequestMethod.PUT)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updatePredavanje(@RequestBody Predavanje predmet, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		Predavanje temp = predavanjeService.getPredavanje(id);
		List<Team> teams = temp.getTeams();
		predmet.setTeams(teams);
		predavanjeService.updatePredavanje(id, predmet);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//works
	@RequestMapping(value="/classes/{predavanjeId}/{teamId}", method=RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public ResponseEntity<?> addTeamToPredavanje(@PathVariable String predavanjeId, @PathVariable String teamId) { //@RequestBody tells spring that the request pay load is going to contain a user
		Predavanje predmet = predavanjeService.getPredavanje(predavanjeId);
		predmet.addTeam(new Team(teamId,"",0));
		predavanjeService.addTeamToPredavanje(predavanjeId, predmet);
		
		Team team = teamService.getGroup(teamId);
		team.setPredavanje(predmet);
		teamService.updateGroup(teamId, team);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/classes/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deletePredmet(@PathVariable String id) {
		predavanjeService.deletePredavanje(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/classes/{predavanjeId}/{teamId}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public ResponseEntity<?> removeTeamFromPredmet(@PathVariable String predavanjeId, @PathVariable String teamId) {
		Predavanje temp = predavanjeService.getPredavanje(predavanjeId);
		temp.removeTeam(new Team(teamId, "", 0));
		predavanjeService.removeTeamfromPredavanje(predavanjeId, temp);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//sort parameters are ID_SORT, IDENTIFIER_SORT
	@RequestMapping(value="/classes/sort/descending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedPredavanjaDescending(@PathVariable List<String> sortParameter) {
		List<Predavanje> predavanja = predavanjeService.getAllPredavanje();
		
		List<PredavanjeComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(PredavanjeComparator.valueOf(s));
		}
		Collections.sort(predavanja, descending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(predavanja, new Predavanje());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/classes/sort/ascending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedPredavanjaAscending(@PathVariable List<String> sortParameter) {
		List<Predavanje> predavanja = predavanjeService.getAllPredavanje();
		
		List<PredavanjeComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(PredavanjeComparator.valueOf(s));
		}
		Collections.sort(predavanja, ascending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(predavanja, new Predavanje());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/classes/listed/{pageNum}/{sizeNum}", method=RequestMethod.GET)
	public HttpEntity<?> getClassesPaginated(@PathVariable int pageNum, @PathVariable int sizeNum) {
		Page<Predavanje> pagePredavanje = predavanjeService.findAll(new PageRequest(pageNum, sizeNum));
		if (pagePredavanje.getTotalPages() != 0) {
			try {
				List<Predavanje> predavanja = pagePredavanje.getContent();
				JsonObject object = formatter.ReturnJSON(predavanja, new Predavanje());
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String timeStamp = new ErrorLog().WriteErorLog(ex);
		        Error error = new Error("http://localhost:8080/error/server", "Internal server error", "Error ID: " + timeStamp);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/classes/listed/{pageNum}/{sizeNum}/descending", method=RequestMethod.GET)
	public HttpEntity<?> getClassesPaginatedSortedBySemester(@PathVariable int pageNum, @PathVariable int sizeNum) {
		Page<Predavanje> pagePredavanje = predavanjeService.findAll(new PageRequest(pageNum, sizeNum));
		if (pagePredavanje.getTotalPages() != 0) {
			try {
				List<Predavanje> predavanja = new ArrayList<Predavanje>(pagePredavanje.getContent());
				//List<Predavanje> predavanja = predavanjeService.getAllPredavanje();
				Collections.sort(predavanja, descending(SEMESTER_SORT));
				JsonObject object = formatter.ReturnJSON(predavanja, new Predavanje());
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String timeStamp = new ErrorLog().WriteErorLog(ex);
		        Error error = new Error("http://localhost:8080/error/server", "Internal server error", "Error ID: " + timeStamp);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}