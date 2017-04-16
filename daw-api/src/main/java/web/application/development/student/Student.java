package web.application.development.student;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.sebastian_daschner.siren4javaee.*;

import web.application.development.predmet.Predmet;
import web.application.development.team.Team;

@Entity 
public class Student{

	@Id	//primary key
	private String id;
	private String name;
	private String email;
	private String number;

	@ManyToMany(targetEntity = Predmet.class)
	private List<Predmet> predmeti;

	public Student(String id, String name, String email, String number) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.number = number;
		this.predmeti = new ArrayList<Predmet>();
	}

	public List<Predmet> getClasses() {
		return predmeti;
	}

	public void setClasses(List<Predmet> classes) {
		this.predmeti = classes;
	}

	public Student() {
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public void enrollIntoClass(Predmet predmet) {
		if (!this.predmeti.contains(predmet)) {
			this.predmeti.add(predmet);
		}
	}

}
