package web.application.development.predavanje;

import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
import web.application.development.team.Team;

@RestController
public class PredavanjeController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private PredavanjeService predavanjeService;
	@Autowired
	private Formatter formatter;
	
	@RequestMapping(value="/classes", method=RequestMethod.GET) //maps URL /predmeti to method getAllPredmeti
	public ResponseEntity<Entity> getAllPredmeti() {
		JsonObject object = formatter.ReturnJSON(predavanjeService.getAllPredavanje(), new Predavanje());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/classes/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getPredavanje(@PathVariable String id) {
		Predavanje predmet = predavanjeService.getPredavanje(id);
		JsonObject object = formatter.ReturnJSON(predmet);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/classes", method=RequestMethod.POST)
	public void addPredavanje(@RequestBody Predavanje predmet) { //@RequestBody tells spring that the request pay load is going to contain a user
		predavanjeService.addPredavanje(predmet);
	}
	/*example of pay load
	 * {
		"id": "java_class",
		"identifier": "J",
		"enrolment": "true"
		}
	 */
	
	@RequestMapping(value="/classes/{predmetId}/{teamId}", method=RequestMethod.POST)
	public void addTeamToPredavanje(@PathVariable String predmetId, @PathVariable String teamId) { //@RequestBody tells spring that the request pay load is going to contain a user
		Predavanje predmet = predavanjeService.getPredavanje(predmetId);
		predmet.addTeam(new Team(teamId,"",0));
		predavanjeService.addTeamToPredavanje(predmetId, predmet);
	}
	
	@RequestMapping(value="/classes/{id}", method=RequestMethod.PUT)
	public void updatePredavanje(@RequestBody Predavanje predmet, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		Predavanje temp = predavanjeService.getPredavanje(id);
		List<Team> teams = temp.getTeams();
		predmet.setTeams(teams);
		predavanjeService.updatePredavanje(id, predmet);
	}
	
	@RequestMapping(value="/classes/{id}", method=RequestMethod.DELETE)
	public void deletePredmet(@PathVariable String id) {
		predavanjeService.deletePredavanje(id);
	}
	
	@RequestMapping(value="/classes/{predmetId}/{teamId}", method=RequestMethod.DELETE)
	public void removeTeamFromPredmet(@PathVariable String predmetId, @PathVariable String teamId) {
		Predavanje temp = predavanjeService.getPredavanje(predmetId);
		temp.removeTeam(new Team(teamId, "", 0));
		predavanjeService.removeTeamfromPredavanje(predmetId, temp);
	}
}
