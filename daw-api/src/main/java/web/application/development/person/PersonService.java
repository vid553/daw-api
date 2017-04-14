package web.application.development.person;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sebastian_daschner.siren4javaee.EntityBuilder;
import com.sebastian_daschner.siren4javaee.Siren;

@Service
public class PersonService {
	
	@Autowired //injects instance of TopicRepository
	private PersonRepository personRepository;

	public List<Person> getAllUsers() {
		List<Person> persons = new ArrayList<>();
		personRepository.findAll().forEach(persons::add);
		return persons;
	}
	
	public Person getUser(String id) {
		//return topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return personRepository.findOne(id);
	}

	public void addUser(Person person) {
		personRepository.save(person);
	}
	
	public void updateUser(String id, Person person) {
		personRepository.save(person);
	}

	public void deleteUser(String id) {
		personRepository.delete(id);
	}
	
	public JsonObject ReturnJSON(Person user) {

		String Uri = "http://localhost:8080/users/" + user.getId();
		JsonObject personEntity = Siren.createEntityBuilder()
			    .addClass("user")
			    .addProperty("id", user.getId())
			    .addProperty("user_type", user.getType())
			    .addProperty("name", user.getName())
			    .addProperty("email", user.getEmail())
			    .addProperty("number", user.getNumber())
			    .addLink(URI.create(Uri), "self")
			    .build();
		
		return personEntity;
	}
	
	public JsonObject ReturnJSON(List<Person> users) {
		String Uri = "http://localhost:8080/users";
		EntityBuilder Users = Siren.createEntityBuilder();
		Users.addClass("users");
		
		for (Person p : users) {
			Users.addEntity(ReturnJSON(p));
		}
		
		Users.addLink(URI.create(Uri), "self");
		return Users.build();
	}
	
}
