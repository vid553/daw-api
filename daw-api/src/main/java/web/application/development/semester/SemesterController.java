package web.application.development.semester;

import static web.application.development.semester.SemesterComparator.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian_daschner.siren4javaee.Entity;
import com.sebastian_daschner.siren4javaee.EntityReader;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.formatter.Formatter;
import web.application.development.predavanje.Predavanje;
import web.application.development.predavanje.PredavanjeService;
import web.application.develeopment.headers.Headers;
import web.application.development.exception.Error;

@RestController
public class SemesterController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private SemesterService semesterService;
	@Autowired 
	private PredavanjeService predavanjeService;
	@Autowired
	private Formatter formatter;
	
	private HttpHeaders problemHeader = Headers.ProblemHeader();
	private HttpHeaders sirenHeader = Headers.SirenHeader();
	
	//works, if non-existing class -> returns 404
	@RequestMapping(value="/semesters", method=RequestMethod.GET) //maps URL /semesters to method getAllSemesters
	public ResponseEntity<?> getAllSemesters() {
		List<Semester> semesters = semesterService.getAllSemesters();
		if (semesters.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			try {
				JsonObject object = formatter.ReturnJSON(semesters, new Semester());
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), errorsInfo[1]);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	//works if semester exists, if non-existing class -> returns 404
	@RequestMapping(value="/semesters/{id}", method=RequestMethod.GET)
	public HttpEntity<?> getSemester(@PathVariable String id) {
		Semester semester = semesterService.getSemester(id);
		if (semester != null) {
			try {
				JsonObject object = formatter.ReturnJSON(semester);
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), errorsInfo[1]);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//works
	@RequestMapping(value="/semesters", method=RequestMethod.POST)
	public ResponseEntity<?> addSemester(@RequestBody Semester semester) { //@RequestBody tells spring that the request pay load is going to contain a user
		semesterService.addSemester(semester);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/semesters/{semesterId}/{predmetId}", method=RequestMethod.POST)
	public ResponseEntity<?> addPredmetToSemester(@PathVariable String semesterId, @PathVariable String predmetId) { 
		Semester semester = semesterService.getSemester(semesterId);
		semester.addPredmet(new Predavanje(predmetId, "", false));
		semesterService.addPredmetToSemester(semesterId, semester);
		
		Predavanje predavanje = predavanjeService.getPredavanje(predmetId);
		predavanje.setSemester(semester);
		predavanjeService.updatePredavanje(predmetId, predavanje);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/semesters/{id}", method=RequestMethod.PUT)
	public ResponseEntity<?> updateSemester(@RequestBody Semester semester, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		//System.out.println("TEST");
		Semester temp = semesterService.getSemester(id);
		List<Predavanje> predmeti = temp.getPredmeti();
		semester.setPredmeti(predmeti);
		semesterService.updateSemester(id, semester);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/semesters/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteSemester(@PathVariable String id) {
		semesterService.deleteSemester(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/semesters/{semesterId}/{predmetId}", method=RequestMethod.DELETE) 
	public ResponseEntity<?> removePredmetFromSemester(@PathVariable String semesterId, @PathVariable String predmetId) {
		Semester temp = semesterService.getSemester(semesterId);
		temp.removePredmet(new Predavanje(predmetId, "", false));
		semesterService.removePredmetFromSemester(semesterId, temp);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//sort parameters are NAME_SORT, ID_SORT, SEASON_SORT, LETO_SORT
	@RequestMapping(value="/semsesters/sort/descending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedSemestersDescending(@PathVariable List<String> sortParameter) {
		List<Semester> semesters = semesterService.getAllSemesters();
		
		List<SemesterComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(SemesterComparator.valueOf(s));
		}
		Collections.sort(semesters, descending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(semesters, new Semester());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/semsesters/sort/ascending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedSemestersAscending(@PathVariable List<String> sortParameter) {
		List<Semester> semesters = semesterService.getAllSemesters();
		
		List<SemesterComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(SemesterComparator.valueOf(s));
		}
		Collections.sort(semesters, ascending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(semesters, new Semester());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
}
