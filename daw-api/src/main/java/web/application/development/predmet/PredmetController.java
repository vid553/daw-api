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

@RestController
public class PredmetController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private PredmetService predmetService;
	@Autowired
	private Formatter formatter;
	
	@RequestMapping(value="/predmeti", method=RequestMethod.GET) //maps URL /predmeti to method getAllPredmeti
	public ResponseEntity<Entity> getAllPredmeti() {
		JsonObject object = formatter.ReturnJSON(predmetService.getAllPredmeti(), new Predmet());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/predmeti/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getPredmet(@PathVariable String id) {
		Predmet predmet = predmetService.getPredmet(id);
		JsonObject object = formatter.ReturnJSON(predmet);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/predmeti", method=RequestMethod.POST)
	public void addPredmet(@RequestBody Predmet predmet) { //@RequestBody tells spring that the request pay load is going to contain a user
		predmetService.addPredmet(predmet);
	}
	
	@RequestMapping(value="/predmeti/{id}", method=RequestMethod.PUT)
	public void updatePredmet(@RequestBody Predmet predmet, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		predmetService.updatePredmet(id, predmet);
	}
	
	@RequestMapping(value="/predmeti/{id}", method=RequestMethod.DELETE)
	public void deletePredmet(@PathVariable String id) {
		predmetService.deletePredmet(id);
	}
	
	/*@RequestMapping(value="/topics/{topicId}/courses", method=RequestMethod.GET) //maps URL /topics to method getAllTopics
	public List<Course> getAllCourses(@PathVariable String topicId) {
		return courseService.getAllCourses();
	}
	
	@RequestMapping(value="/topics/{topicId}/courses/{courseId}", method=RequestMethod.GET) //{} tells spring the containing part is a variable
	public Course getCourse(@PathVariable String courseId) { //annotation that maps {id} to String id
		return courseService.getCourse(courseId);
	}
	
	@RequestMapping(value="/topics/{topicId}/courses", method=RequestMethod.POST)
	public void addCourse(@RequestBody Course course, @PathVariable String topicId) { //@RequestBody tells spring that the request pay load is going to contain a course
		course.setTopic(new Topic(topicId, "","", ""));
		courseService.addCourse(course);
	}
	
	@RequestMapping(value="/topics/{topicId}/courses/{courseId}", method=RequestMethod.PUT)
	public void updateCourse(@RequestBody Course course, @PathVariable String courseId, @PathVariable String topicId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		course.setTopic(new Topic(topicId, "","", ""));
		courseService.updateCourse(course);
	}
	
	@RequestMapping(value="/topics/{topicId}/courses/{courseId}", method=RequestMethod.DELETE)
	public void deleteCourse(@PathVariable String courseId) {
		courseService.deleteCourse(courseId);
	} */
}
