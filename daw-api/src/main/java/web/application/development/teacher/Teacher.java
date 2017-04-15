package web.application.development.teacher;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.sebastian_daschner.siren4javaee.*;

import web.application.development.course.Course;
import web.application.development.predmet.Predmet;
import web.application.development.student.Student;

@Entity 
public class Teacher{

	@Id	//primary key
	private String id;
	private String name;
	private String email;
	private String number;
	private Boolean admin;
	
	@OneToMany
	private List<Course> courses;

	public Teacher(String id, String name, String email, String number, Boolean admin) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.number = number;
		this.admin = admin;
		courses = new ArrayList<Course>();
	}
	
	public Teacher() {
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
	
	public void setAdmin() {
		this.admin = true;
	}
	
	public Boolean getAdmin() {
		return admin;
	}
	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	public void addCourse(Course course) {
		this.courses.add(course);
	}
	
	public void removeCourse(Course course) {
		List<Course> courses = new ArrayList<Course>();
		for(Course c : this.courses){
		    if(c.getId().equals(course.getId())) {
		    	courses.add(c);
		    }
		}
		this.courses.removeAll(courses);
	}
}
