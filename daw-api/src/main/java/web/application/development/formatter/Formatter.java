package web.application.development.formatter;

import java.net.URI;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.stereotype.Component;

import com.sebastian_daschner.siren4javaee.EntityBuilder;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.teacher.Teacher;
import web.application.development.student.Student;

@Component
public class Formatter {

	//returns Siren representation of Student
	public JsonObject ReturnJSON(Student student) {

		String Uri = "http://localhost:8080/students/" + student.getId();
		JsonObject studentEntity = Siren.createEntityBuilder()
			    .addClass("student")
			    .addProperty("id", student.getId())
			    .addProperty("name", student.getName())
			    .addProperty("email", student.getEmail())
			    .addProperty("number", student.getNumber())
			    .addLink(URI.create(Uri), "self")
			    .build();
		
		return studentEntity;
	}
	
		public JsonObject ReturnJSON(Teacher teacher) {

			String Uri = "http://localhost:8080/teachers/" + teacher.getId();
			JsonObject personEntity = Siren.createEntityBuilder()
				    .addClass("teacher")
				    .addProperty("id", teacher.getId())
				    .addProperty("name", teacher.getName())
				    .addProperty("email", teacher.getEmail())
				    .addProperty("number", teacher.getNumber())
				    .addProperty("admin", teacher.getAdmin())
				    .addLink(URI.create(Uri), "self")
				    .build();
			
			return personEntity;
		}
	
	//returns Siren representation of a list of teachers
	public JsonObject ReturnJSON(List<Teacher> teachers, Teacher teach) {
		String Uri = "http://localhost:8080/teachers";
		EntityBuilder Teachers = Siren.createEntityBuilder();
		Teachers.addClass("teachers");
		
		for (Teacher p : teachers) {
			Teachers.addEntity(ReturnJSON(p));
		}
		
		Teachers.addLink(URI.create(Uri), "self");
		return Teachers.build(); 
	}
    
	//returns Siren representation of a list of students, has dummy argument because of problems with erasure
	public JsonObject ReturnJSON(List<Student> students, Student pers) {
		String Uri = "http://localhost:8080/students";
		EntityBuilder Students = Siren.createEntityBuilder();
		Students.addClass("students");
		
		for (Student p : students) {
			Students.addEntity(ReturnJSON(p));
		}
		
		Students.addLink(URI.create(Uri), "self");
		return Students.build();
	}

}
