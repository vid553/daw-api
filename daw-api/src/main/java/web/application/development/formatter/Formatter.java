package web.application.development.formatter;

import java.net.URI;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sebastian_daschner.siren4javaee.EntityBuilder;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.teacher.Teacher;
import web.application.development.team.Team;
import web.application.development.student.Student;
import web.application.development.course.Course;
import web.application.development.predavanje.Predavanje;
import web.application.development.semester.Semester;


@Component
public class Formatter {

	///////////////////////////////////////////////////////////////////////////////////////////
	//first layer methods
	
	public JsonObject ReturnJSON(List<Predavanje> predavanja, Object placeholder) {
		String Uri = "http://localhost:8080/classes";
		EntityBuilder Classes = Siren.createEntityBuilder();
		Classes.addClass("classes");
		
		for (Predavanje p : predavanja) {
			Classes.addEntity(ReturnJSON(p, new Predavanje()));
		}
		
		Classes.addLink(URI.create(Uri), "self");
		return Classes.build();
	}	

	public JsonObject ReturnJSON(Predavanje predavanje) {

		String Uri = "http://localhost:8080/classes/" + predavanje.getId();
		EntityBuilder predavanjeEntity = Siren.createEntityBuilder()
			    .addClass("class")
			    .addProperty("id", predavanje.getId())
			    .addProperty("identifier", predavanje.getIdentifier())
			    .addProperty("enrolment_auto", predavanje.getEnrolment());
		
		//find configuration for null orperties and empty arrays in jackson
		
		List<Team> groups = predavanje.getTeams();
		for (Team g : groups) {
			predavanjeEntity.addEntity(ReturnJSON(g, new Team()));
		}
		
		List<Student> students = predavanje.getStudents();
		
		for (Student s : students) {
			predavanjeEntity.addEntity(ReturnJSON(s, new Predavanje()));
		}
		
		List<Teacher> teachers = predavanje.getTeachers();
		
		for (Teacher t : teachers) {
			predavanjeEntity.addEntity(ReturnJSON(t, new Predavanje()));
		}
		
		Course course = predavanje.getCourse();
		predavanjeEntity.addEntity(ReturnJSON(course, new Predavanje()));
		
		Semester semester = predavanje.getSemester();
		predavanjeEntity.addEntity(ReturnJSON(semester, new Predavanje()));
		
	    predavanjeEntity.addLink(URI.create(Uri), "self");
		
		return predavanjeEntity.build();
	}

	public JsonObject ReturnJSON(List<Student> students, Student student) {
		String Uri = "http://localhost:8080/students";
		EntityBuilder Students = Siren.createEntityBuilder();
		Students.addClass("students");
		
		for (Student s : students) {
			Students.addEntity(ReturnJSON(s, new Student()));
		}
		
		Students.addLink(URI.create(Uri), "self");
		return Students.build();
	}

	public JsonObject ReturnJSON(Student student) {
		String Uri = "http://localhost:8080/students/" + student.getId();
		EntityBuilder studentEntity = Siren.createEntityBuilder()
			    .addClass("student")
			    .addProperty("id", student.getId())
			    .addProperty("name", student.getName())
			    .addProperty("email", student.getEmail())
			    .addProperty("number", student.getNumber());

		List<Predavanje> predavanja = student.getClasses();
		
		for (Predavanje p : predavanja) {
			studentEntity.addEntity(ReturnJSON(p, new Student()));
		}
		
		studentEntity.addLink(URI.create(Uri), "self");
		return studentEntity.build();
	}
	
	public JsonObject ReturnJSON(List<Team> groups, Team group) {
		String Uri = "http://localhost:8080/groups";
		EntityBuilder Groups = Siren.createEntityBuilder();
		Groups.addClass("groups");
		
		for (Team g : groups) {
			Groups.addEntity(ReturnJSON(g, new Team()));
		}
		
		Groups.addLink(URI.create(Uri), "self");
		return Groups.build();
	}
	
	public JsonObject ReturnJSON(Team group) {
		String Uri = "http://localhost:8080/groups/" + group.getId();
		EntityBuilder groupEntity = Siren.createEntityBuilder()
				.addClass("group")
				.addProperty("id", group.getId())
				.addProperty("name", group.getName())
				.addProperty("students_limit", group.getStudents_limit());
		
		List<Student> students = group.getStudents();
		
		for (Student s : students) {
			groupEntity.addEntity(ReturnJSON(s, new Student()));
		}
		
		Predavanje predavanje = group.getPredavanje();
		
		groupEntity.addEntity(ReturnJSON(predavanje, new Team()));
		
		groupEntity.addLink(URI.create(Uri), "self");
		
		return groupEntity.build();
	}
	
	public JsonObject ReturnJSON(List<Semester> semesters, Semester placeholder) {
		String Uri = "http://localhost:8080/semesters";
		EntityBuilder Semesters = Siren.createEntityBuilder();
		Semesters.addClass("semesters");
		
		for (Semester s : semesters) {
			Semesters.addEntity(ReturnJSON(s, new Semester()));
		}
		
		Semesters.addLink(URI.create(Uri), "self");
		return Semesters.build();
	}
	
	//returns sememster with parameters, entities are Predavanja and link to predavanje.self
	public JsonObject ReturnJSON(Semester semester) {
		String Uri = "http://localhost:8080/semesters/" + semester.getId();
		EntityBuilder semesterEntity = Siren.createEntityBuilder()
				.addClass("semester")
				.addProperty("id", semester.getId())
				.addProperty("name", semester.getName())
				.addProperty("season", semester.getSeason())
				.addProperty("leto", semester.getLeto());
		
		List<Predavanje> predavanja = semester.getPredmeti();
		
		for (Predavanje p : predavanja) {
			semesterEntity.addEntity(ReturnJSON(p, new Semester()));
		}
		
		semesterEntity.addLink(URI.create(Uri), "self");
		
		return semesterEntity.build();
	}
	
	public JsonObject ReturnJSON(List<Course> courses, Course placeholder) {
		String Uri = "http://localhost:8080/courses";
		EntityBuilder Courses = Siren.createEntityBuilder();
		Courses.addClass("courses");
		
		for (Course c : courses) {
			Courses.addEntity(ReturnJSON(c, new Course()));
		}
		
		Courses.addLink(URI.create(Uri), "self");
		return Courses.build();
	}
	
	public JsonObject ReturnJSON(Course course) {
		String Uri = "http://localhost:8080/courses/" + course.getId();
		EntityBuilder courseEntity = Siren.createEntityBuilder()
				.addClass("course")
				.addProperty("id", course.getId())
				.addProperty("name", course.getName())
				.addProperty("acronim", course.getAcronim());
		
		List<Predavanje> predavanja = course.getClasses();
		for (Predavanje p : predavanja) {
			courseEntity.addEntity(ReturnJSON(p, new Predavanje()));
		}
		
		Teacher teacher = course.getTeacher();
		courseEntity.addEntity(ReturnJSON(teacher, new Course()));
		
		courseEntity.addLink(URI.create(Uri), "self");
		return courseEntity.build();
	}
	
	public JsonObject ReturnJSON(List<Teacher> teachers, Teacher placeholder) {
		String Uri = "http://localhost:8080/teachers";
		EntityBuilder Teachers = Siren.createEntityBuilder();
		Teachers.addClass("teachers");
		
		for (Teacher t : teachers) {
			Teachers.addEntity(ReturnJSON(t, new Teacher()));
		}
		
		Teachers.addLink(URI.create(Uri), "self");
		return Teachers.build();
	}
	
	public JsonObject ReturnJSON(Teacher teacher) {
		String Uri = "http://localhost:8080/teachers/" + teacher.getId();
		EntityBuilder teacherEntity = Siren.createEntityBuilder()
				.addClass("teacher")
				.addProperty("id", teacher.getId())
				.addProperty("name", teacher.getName())
				.addProperty("number", teacher.getNumber())
				.addProperty("email", teacher.getEmail())
				.addProperty("admin", teacher.getAdmin());
		
		List<Course> courses = teacher.getCourses();
		for (Course c : courses) {
			teacherEntity.addEntity(ReturnJSON(c, new Teacher()));
		}
		
		List<Predavanje> predavanja = teacher.getPredavanja();
		for (Predavanje p : predavanja) {
			teacherEntity.addEntity(ReturnJSON(p, new Teacher()));
		}
		
		teacherEntity.addLink(URI.create(Uri), "self");
		
		return teacherEntity.build();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	//second layer methods
	//returns predavanje with properties and link to self, used in second layer of representation
	public JsonObject ReturnJSON(Predavanje predavanje, Object placeholder) {
		String Uri = "http://localhost:8080/classes/" + predavanje.getId();
		EntityBuilder predavanjeEntity = Siren.createEntityBuilder()
				.setTitle("class")
				.addClass("class")
				.addProperty("id", predavanje.getId())
				.addProperty("identifier", predavanje.getIdentifier())
				.addProperty("enrolment_auto", predavanje.getEnrolment())
				.addLink(URI.create(Uri), "self");
		
		return predavanjeEntity.build();
	}
	
	//returns Siren representation of Teacher with properties and link to self
	public JsonObject ReturnJSON(Teacher teacher, Object placeholder) {

		String Uri = "http://localhost:8080/teachers/" + teacher.getId();
		EntityBuilder teacherEntity = Siren.createEntityBuilder()
				.setTitle("teacher")
			    .addClass("teacher")
			    .addProperty("id", teacher.getId())
			    .addProperty("name", teacher.getName())
			    .addProperty("email", teacher.getEmail())
			    .addProperty("number", teacher.getNumber())
			    .addProperty("admin", teacher.getAdmin())
				.addLink(URI.create(Uri), "self");

		return teacherEntity.build();
	}
	
	//returns siren representation of Course with properties and link to self
	public JsonObject ReturnJSON(Course course, Object placeholder) {
		String Uri = "http://localhost:8080/courses/" + course.getId();
		EntityBuilder courseEntity = Siren.createEntityBuilder()
				.setTitle("course")
				.addClass("course")
				.addProperty("id", course.getId())
				.addProperty("name", course.getAcronim())
				.addProperty("acronim", course.getAcronim())
				.addLink(URI.create(Uri), "self");
		
		return courseEntity.build();
	}
	
	public JsonObject ReturnJSON(Student student, Object placeholder) {
		String Uri = "http://localhost:8080/students/" + student.getId();
		EntityBuilder studentEntity = Siren.createEntityBuilder()
				.setTitle("student")
				.addClass("student")
				.addProperty("id", student.getId())
				.addProperty("name", student.getName())
				.addProperty("number", student.getNumber())
				.addProperty("email", student.getEmail())
				.addLink(URI.create(Uri), "self");
		
		return studentEntity.build();
	}
	
	public JsonObject ReturnJSON(Team group, Object placeholder) {
		String Uri = "http://localhost:8080/groups/" + group.getId();
		EntityBuilder groupEntity = Siren.createEntityBuilder()
				.setTitle("group")
				.addClass("group")
				.addProperty("id", group.getId())
				.addProperty("name", group.getName())
				.addProperty("students_limit", group.getStudents_limit())
				.addLink(URI.create(Uri), "self");
		
		return groupEntity.build();
	}
	
	public JsonObject ReturnJSON(Semester semester, Object placeholder) {
		String Uri = "http://localhost:8080/semesters/" + semester.getId();
		EntityBuilder semesterEntity = Siren.createEntityBuilder()
				.setTitle("semester")
			    .addClass("semester")
			    .addProperty("id", semester.getId())
			    .addProperty("name", semester.getName())
			    .addProperty("season", semester.getSeason())
			    .addProperty("leto", semester.getLeto())
				.addLink(URI.create(Uri), "self");
		
		return semesterEntity.build();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
}
