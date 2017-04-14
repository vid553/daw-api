package web.application.development.teacher;

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
public class TeacherController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private TeacherService teacherService;
	@Autowired
	private Formatter formatter;
	
	@RequestMapping(value="/teachers", method=RequestMethod.GET) //maps URL /topics to method getAllTopics
	public ResponseEntity<Entity> getAllUsers() {
		JsonObject object = formatter.ReturnJSON(teacherService.getAllTeachers(), new Teacher());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getTeacher(@PathVariable String id) {
		Teacher teacher = teacherService.getTeacher(id);
		
		JsonObject object = formatter.ReturnJSON(teacher);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers", method=RequestMethod.POST)
	public void addUser(@RequestBody Teacher teacher) { //@RequestBody tells spring that the request pay load is going to contain a user
		teacherService.addTeacher(teacher);
	}
	
	@RequestMapping(value="/teachers/{id}", method=RequestMethod.PUT)
	public void updateUser(@RequestBody Teacher teacher, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		teacherService.updateTeacher(id, teacher);
	}
	
	@RequestMapping(value="/teachers/{id}", method=RequestMethod.DELETE)
	public void deleteUser(@PathVariable String id) {
		teacherService.deleteTeacher(id);
	}

}
