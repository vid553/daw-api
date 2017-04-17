package web.application.development.formatter;

import java.net.URI;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.stereotype.Component;

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
	public JsonObject ReturnJSON(List<Semester> semesters, Semester placeholder) {
		String Uri = "http://localhost:8080/semesters";
		EntityBuilder Semesters = Siren.createEntityBuilder();
		Semesters.addClass("semesters");
		
		for (Semester s : semesters) {
			Semesters.addEntity(ReturnJSON(s));
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
			Courses.addEntity(ReturnJSON(c));
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
		courseEntity.addEntity(ReturnJSON(teacher, new Teacher()));
		
		courseEntity.addLink(URI.create(Uri), "self");
		return courseEntity.build();
	}
	
	public JsonObject ReturnJSON(List<Teacher> teachers, Teacher placeholder) {
		String Uri = "http://localhost:8080/teachers";
		EntityBuilder Teachers = Siren.createEntityBuilder();
		Teachers.addClass("teachers");
		
		for (Teacher t : teachers) {
			Teachers.addEntity(ReturnJSON(t));
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
			teacherEntity.addEntity(ReturnJSON(c, new Course()));
		}
		
		List<Predavanje> predavanja = teacher.getPredavanja();
		for (Predavanje p : predavanja) {
			teacherEntity.addEntity(ReturnJSON(p, new Predavanje()));
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
				.addClass("course")
				.addProperty("id", course.getId())
				.addProperty("name", course.getAcronim())
				.addProperty("acronim", course.getAcronim())
				.addLink(URI.create(Uri), "self");
		
		return courseEntity.build();
	}
	///////////////////////////////////////////////////////////////////////////////////////////
	
	//returns Siren representation of Student
	public JsonObject ReturnJSON(Student student) {

		String Uri = "http://localhost:8080/students/" + student.getId();
		EntityBuilder studentEntity = Siren.createEntityBuilder()
			    .addClass("student")
			    .addProperty("id", student.getId())
			    .addProperty("name", student.getName())
			    .addProperty("email", student.getEmail())
			    .addProperty("number", student.getNumber());
		
		List<Predavanje> predmeti = student.getClasses();
		
		for (Predavanje p : predmeti) {
			studentEntity.addEntity(ReturnJSON(p));
		}
		
			    studentEntity.addLink(URI.create(Uri), "self");

		return studentEntity.build();
	}
	
	//returns Siren representation of Student
	public JsonObject ReturnJSON(Student student, Predavanje predmet) {

		String Uri = "http://localhost:8080/students/" + student.getId();
		EntityBuilder studentEntity = Siren.createEntityBuilder()
			    .addClass("student")
			    .addProperty("id", student.getId())
			    .addProperty("name", student.getName())
			    .addProperty("email", student.getEmail())
			    .addProperty("number", student.getNumber());
		/*
		List<Predmet> predmeti = student.getClasses();
		
		for (Predmet p : predmeti) {
			studentEntity.addEntity(ReturnJSON(p));
		}
		*/
			    studentEntity.addLink(URI.create(Uri), "self");

		return studentEntity.build();
	}
	/*
	//returns Siren representation of Teacher
	public JsonObject ReturnJSON(Teacher teacher) {

		String Uri = "http://localhost:8080/teachers/" + teacher.getId();
		EntityBuilder teacherEntity = Siren.createEntityBuilder()
			    .addClass("teacher")
			    .addProperty("id", teacher.getId())
			    .addProperty("name", teacher.getName())
			    .addProperty("email", teacher.getEmail())
			    .addProperty("number", teacher.getNumber())
			    .addProperty("admin", teacher.getAdmin());
		
				List<Course> courses = teacher.getCourses();
				
				for (Course c : courses) {
					teacherEntity.addEntity(ReturnJSON(c));
				}
				
				List<Predavanje> predmets = teacher.getPredavanja();
				
				for (Predavanje p : predmets) {
					teacherEntity.addEntity(ReturnJSON(p));
				}
				
				teacherEntity.addLink(URI.create(Uri), "self");
			    //.addLink(URI.create(Uri), "self")
			    //.build();
			
		return teacherEntity.build();
	}

*/
	
	public JsonObject ReturnJSON(Team team) {

		String Uri = "http://localhost:8080/groups/" + team.getId();
		EntityBuilder teamEntity = Siren.createEntityBuilder()
			    .addClass("group")
			    .addProperty("id", team.getId())
			    .addProperty("name", team.getName())
			    .addProperty("students_limit", team.getStudents_limit());
			    
				List<Student> students = team.getStudents();
		
				for (Student s : students) {
					teamEntity.addEntity(ReturnJSON(s));
				}
		
			    teamEntity.addLink(URI.create(Uri), "self");
			    //.build();
		
		return teamEntity.build();
	}
		/*
	//returns Siren representation of Course
	public JsonObject ReturnJSON(Course course) {

		String Uri = "http://localhost:8080/courses/" + course.getId();
		EntityBuilder courseEntity = Siren.createEntityBuilder()
			    .addClass("course")
			    .addProperty("id", course.getId())
			    .addProperty("name", course.getName())
			    .addProperty("acronim", course.getAcronim());
		
		List<Predavanje> classes = course.getClasses();
	
			for (Predavanje p : classes) {
				courseEntity.addEntity(ReturnJSON(p));
			}
		    courseEntity.addLink(URI.create(Uri), "self");
		
		return courseEntity.build();
	}*/
	/*
	//returns Siren representation of Semester
	public JsonObject ReturnJSON(Semester semester) {

		String Uri = "http://localhost:8080/semesters/" + semester.getId();
		EntityBuilder semesterEntity = Siren.createEntityBuilder()
			    .addClass("semester")
			    .addProperty("id", semester.getId())
			    .addProperty("name", semester.getName())
			    .addProperty("season", semester.getSeason())
			    .addProperty("leto", semester.getLeto());
			    
		List<Predavanje> predmeti = semester.getPredmeti();
		for (Predavanje p : predmeti) {
			semesterEntity.addEntity(ReturnJSON(p));
		}
		semesterEntity.addLink(URI.create(Uri), "self");
		
		return semesterEntity.build();
	}
	*/
	public JsonObject ReturnJSON(Semester semester, Predavanje pre) {

		String Uri = "http://localhost:8080/semesters/" + semester.getId();
		EntityBuilder semesterEntity = Siren.createEntityBuilder()
			    .addClass("semester")
			    .addProperty("id", semester.getId())
			    .addProperty("name", semester.getName())
			    .addProperty("season", semester.getSeason())
			    .addProperty("leto", semester.getLeto());
		/*	    
		List<Predavanje> predmeti = semester.getPredmeti();
		for (Predavanje p : predmeti) {
			semesterEntity.addEntity(ReturnJSON(p));
		}*/
		semesterEntity.addLink(URI.create(Uri), "self");
		
		return semesterEntity.build();
	}
	
	//returns Siren representation of class
	public JsonObject ReturnJSON(Predavanje predmet) {

		String Uri = "http://localhost:8080/classes/" + predmet.getId();
		EntityBuilder predmetEntity = Siren.createEntityBuilder()
			    .addClass("class")
			    .addProperty("id", predmet.getId())
			    .addProperty("identifier", predmet.getIdentifier())
			    .addProperty("enrolment_auto", predmet.getEnrolment());
		
		List<Team> teams = predmet.getTeams();
		for (Team t : teams) {
			predmetEntity.addEntity(ReturnJSON(t));
		}
		
		List<Student> students = predmet.getStudents();
		
		for (Student s : students) {
			predmetEntity.addEntity(ReturnJSON(s, new Predavanje()));
		}
		
		List<Teacher> teachers = predmet.getTeachers();
		
		for (Teacher t : teachers) {
			predmetEntity.addEntity(ReturnJSON(t, new Predavanje()));
		}
		
		predmetEntity.addEntity(ReturnJSON(predmet.getSemester(), new Predavanje()));
		
	    predmetEntity.addLink(URI.create(Uri), "self");
		
		return predmetEntity.build();
	}

	/*		
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
    */
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
/*
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
	}*/
	/*
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
	*/
	//returns Siren representation of a list of classes, has dummy argument because of problems with erasure
		public JsonObject ReturnJSON(List<Predavanje> predmeti, Predavanje pre) {
			String Uri = "http://localhost:8080/classes";
			EntityBuilder Predmeti = Siren.createEntityBuilder();
			Predmeti.addClass("classes");
			
			for (Predavanje p : predmeti) {
				Predmeti.addEntity(ReturnJSON(p));
			}
			
			Predmeti.addLink(URI.create(Uri), "self");
			return Predmeti.build();
		}
		
		public JsonObject ReturnJSON(List<Team> groups, Team team) {
			String Uri = "http://localhost:8080/groups";
			EntityBuilder Groups = Siren.createEntityBuilder();
			Groups.addClass("groups");
			
			for (Team p : groups) {
				Groups.addEntity(ReturnJSON(p));
			}
			
			Groups.addLink(URI.create(Uri), "self");
			return Groups.build();
	}
}
