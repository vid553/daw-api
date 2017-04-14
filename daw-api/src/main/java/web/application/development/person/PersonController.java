package web.application.development.person;

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
import web.application.development.hello.Hello;
import web.application.development.hello.HelloController;

@RestController
public class PersonController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private PersonService personService;
	@Autowired
	private Formatter formatter;
	
	@RequestMapping(value="/users", method=RequestMethod.GET) //maps URL /topics to method getAllTopics
	public ResponseEntity<Entity> getAllUsers() {
		JsonObject object = formatter.ReturnJSON(personService.getAllUsers());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getUser(@PathVariable String id) {
		Person person = personService.getUser(id);
		
		JsonObject object = formatter.ReturnJSON(person);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/users", method=RequestMethod.POST)
	public void addUser(@RequestBody Person user) { //@RequestBody tells spring that the request pay load is going to contain a user
		personService.addUser(user);
	}
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
	public void updateUser(@RequestBody Person user, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		personService.updateUser(id, user);
	}
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.DELETE)
	public void deleteUser(@PathVariable String id) {
		personService.deleteUser(id);
	}

}
