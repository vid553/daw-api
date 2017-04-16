package web.application.development.semester;

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
import web.application.development.predavanje.Predavanje;

@RestController
public class SemesterController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private SemesterService semesterService;
	@Autowired
	private Formatter formatter;
	
	@RequestMapping(value="/semesters", method=RequestMethod.GET) //maps URL /semesters to method getAllSemesters
	public ResponseEntity<Entity> getAllSemesters() {
		JsonObject object = formatter.ReturnJSON(semesterService.getAllSemesters(), new Semester());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/semesters/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getSemester(@PathVariable String id) {
		Semester semester = semesterService.getSemester(id);
		JsonObject object = formatter.ReturnJSON(semester);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/semesters", method=RequestMethod.POST)
	public void addSemester(@RequestBody Semester semester) { //@RequestBody tells spring that the request pay load is going to contain a user
		semesterService.addSemester(semester);
	}
	
	@RequestMapping(value="/semesters/{semesterId}/{predmetId}", method=RequestMethod.POST)
	public void addPredmetToSemester(@PathVariable String semesterId, @PathVariable String predmetId) { //@RequestBody tells spring that the request pay load is going to contain a user
		Semester semester = semesterService.getSemester(semesterId);
		semester.addPredmet(new Predavanje(predmetId, "", false));
		semesterService.addPredmetToSemester(semesterId, semester);
	}
	
	@RequestMapping(value="/semesters/{id}", method=RequestMethod.PUT)
	public void updateSemester(@RequestBody Semester semester, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		Semester temp = semesterService.getSemester(id);
		List<Predavanje> predmeti = temp.getPredmeti();
		semester.setPredmeti(predmeti);
		semesterService.updateSemester(id, semester);
	}
	
	@RequestMapping(value="/semesters/{id}", method=RequestMethod.DELETE)
	public void deleteSemester(@PathVariable String id) {
		semesterService.deleteSemester(id);
	}
	
	@RequestMapping(value="/semesters/{semesterId}/{predmetId}", method=RequestMethod.DELETE) //changes class in semester
	public void removePredmetFromSemester(@PathVariable String semesterId, @PathVariable String predmetId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Semester temp = semesterService.getSemester(semesterId);
		temp.removePredmet(new Predavanje(predmetId, "", false));
		semesterService.removePredmetFromSemester(semesterId, temp);
	}
}
