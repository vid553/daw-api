package web.application.development.student;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian_daschner.siren4javaee.Entity;
import com.sebastian_daschner.siren4javaee.EntityReader;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.formatter.Formatter;

@RestController
public class StudentController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private StudentService personService;
	@Autowired
	private Formatter formatter;
	
	@RequestMapping(value="/students", method=RequestMethod.GET) //maps URL /topics to method getAllTopics
	public ResponseEntity<Entity> getAllUsers() {
		JsonObject object = formatter.ReturnJSON(personService.getAllStudents(), new Student());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/students/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getUser(@PathVariable String id) {
		Student student = personService.getStudent(id);
		
		JsonObject object = formatter.ReturnJSON(student);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/students", method=RequestMethod.POST)
	public void addUser(@RequestBody Student student) { //@RequestBody tells spring that the request pay load is going to contain a user
		personService.addStudent(student);
	}
	
	@RequestMapping(value="/students/{id}", method=RequestMethod.PUT)
	public void updateUser(@RequestBody Student student, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		personService.updateStudent(id, student);
	}
	
	@RequestMapping(value="/students/{id}", method=RequestMethod.DELETE)
	public void deleteUser(@PathVariable String id) {
		personService.deleteStudent(id);
	}

}
