package web.application.development.formatter;

import java.net.URI;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.stereotype.Component;

import com.sebastian_daschner.siren4javaee.EntityBuilder;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.person.Person;

@Component
public class Formatter {

	//returns Siren representation of User
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
	
	//returns Siren representation of a list of users
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
