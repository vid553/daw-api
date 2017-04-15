package web.application.development.formatter;

import java.net.URI;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.stereotype.Component;

import com.sebastian_daschner.siren4javaee.EntityBuilder;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.teacher.Teacher;
import web.application.development.student.Student;
import web.application.development.course.Course;
import web.application.development.semester.Semester;
import web.application.development.predmet.Predmet;

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
	
	//returns Siren representation of Teacher
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
		
	//returns Siren representation of Course
	public JsonObject ReturnJSON(Course course) {

		String Uri = "http://localhost:8080/courses/" + course.getId();
		JsonObject semesterEntity = Siren.createEntityBuilder()
			    .addClass("course")
			    .addProperty("id", course.getId())
			    .addProperty("name", course.getName())
			    .addProperty("acronim", course.getAcronim())
			    .addLink(URI.create(Uri), "self")
			    .build();
		
		return semesterEntity;
	}
	
	//returns Siren representation of Semester
	public JsonObject ReturnJSON(Semester semester) {

		String Uri = "http://localhost:8080/semesters/" + semester.getId();
		JsonObject semesterEntity = Siren.createEntityBuilder()
			    .addClass("semester")
			    .addProperty("id", semester.getId())
			    .addProperty("name", semester.getName())
			    .addProperty("season", semester.getSeason())
			    .addProperty("leto", semester.getLeto())
			    .addLink(URI.create(Uri), "self")
			    .build();
		
		return semesterEntity;
	}
	
	//returns Siren representation of class
	public JsonObject ReturnJSON(Predmet predmet) {

		String Uri = "http://localhost:8080/predmeti/" + predmet.getId();
		JsonObject semesterEntity = Siren.createEntityBuilder()
			    .addClass("predmet")
			    .addProperty("id", predmet.getId())
			    .addProperty("identifier", predmet.getIdentifier())
			     .addProperty("enrolment_auto", predmet.getEnrolment())
			    .addLink(URI.create(Uri), "self")
			    .build();
		
		return semesterEntity;
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

	//returns Siren representation of a list of courses, has dummy argument because of problems with erasure
	public JsonObject ReturnJSON(List<Course> courses, Course cor) {
		String Uri = "http://localhost:8080/courses";
		EntityBuilder Courses = Siren.createEntityBuilder();
		Courses.addClass("courses");
		
		for (Course p : courses) {
			Courses.addEntity(ReturnJSON(p));
		}
		
		Courses.addLink(URI.create(Uri), "self");
		return Courses.build();
	}
	
	//returns Siren representation of a list of semesters, has dummy argument because of problems with erasure
	public JsonObject ReturnJSON(List<Semester> semesters, Semester sem) {
		String Uri = "http://localhost:8080/semesters";
		EntityBuilder Semesters = Siren.createEntityBuilder();
		Semesters.addClass("semesters");
		
		for (Semester p : semesters) {
			Semesters.addEntity(ReturnJSON(p));
		}
		
		Semesters.addLink(URI.create(Uri), "self");
		return Semesters.build();
	}
	
	//returns Siren representation of a list of classes, has dummy argument because of problems with erasure
		public JsonObject ReturnJSON(List<Predmet> predmeti, Predmet pre) {
			String Uri = "http://localhost:8080/predmeti";
			EntityBuilder Predmeti = Siren.createEntityBuilder();
			Predmeti.addClass("predmeti");
			
			for (Predmet p : predmeti) {
				Predmeti.addEntity(ReturnJSON(p));
			}
			
			Predmeti.addLink(URI.create(Uri), "self");
			return Predmeti.build();
		}
}
