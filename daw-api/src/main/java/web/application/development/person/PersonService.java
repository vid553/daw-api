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
	
}
