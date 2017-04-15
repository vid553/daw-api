package web.application.development.predmet;

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
public class PredmetController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private PredmetService predmetService;
	@Autowired
	private Formatter formatter;
	
	@RequestMapping(value="/classes", method=RequestMethod.GET) //maps URL /predmeti to method getAllPredmeti
	public ResponseEntity<Entity> getAllPredmeti() {
		JsonObject object = formatter.ReturnJSON(predmetService.getAllPredmeti(), new Predmet());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/classes/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getPredmet(@PathVariable String id) {
		Predmet predmet = predmetService.getPredmet(id);
		JsonObject object = formatter.ReturnJSON(predmet);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/classes", method=RequestMethod.POST)
	public void addPredmet(@RequestBody Predmet predmet) { //@RequestBody tells spring that the request pay load is going to contain a user
		predmetService.addPredmet(predmet);
	}
	
	@RequestMapping(value="/classes/{predmetId}/{teamId}", method=RequestMethod.POST)
	public void addTeamToPredmet(@PathVariable String predmetId, @PathVariable String teamId) { //@RequestBody tells spring that the request pay load is going to contain a user
		Predmet predmet = predmetService.getPredmet(predmetId);
		predmet.addTeam(new Team(teamId,"",0));
		predmetService.addTeamToPredmet(predmetId, predmet);
	}
	
	@RequestMapping(value="/classes/{id}", method=RequestMethod.PUT)
	public void updatePredmet(@RequestBody Predmet predmet, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		Predmet temp = predmetService.getPredmet(id);
		List<Team> teams = temp.getTeams();
		predmet.setTeams(teams);
		predmetService.updatePredmet(id, predmet);
	}
	
	@RequestMapping(value="/classes/{id}", method=RequestMethod.DELETE)
	public void deletePredmet(@PathVariable String id) {
		predmetService.deletePredmet(id);
	}
	
	@RequestMapping(value="/classes/{predmetId}/{teamId}", method=RequestMethod.DELETE)
	public void removeTeamFromPredmet(@PathVariable String predmetId, @PathVariable String teamId) {
		Predmet temp = predmetService.getPredmet(predmetId);
		temp.removeTeam(new Team(teamId, "", 0));
		predmetService.removeTeamfromPredmet(predmetId, temp);
	}
}
