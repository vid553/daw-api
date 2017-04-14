package web.application.development.team;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import web.application.development.student.Student;
import web.application.development.topic.Topic;

@Entity 
public class Team{

	@Id	//primary key
	private String id;
	private String name;
	private int students_limit;
	
	@OneToMany
	private List<Student> students;
	
	public Team(String id, String name, int students_limit) {
		super();
		this.id = id;
		this.name = name;
		this.students_limit = students_limit;
		students = new ArrayList<Student>();
		//this.student = new Student("", "", "", "");
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public int getStudents_limit() {
		return students_limit;
	}

	public void setStudents_limit(int students_limit) {
		this.students_limit = students_limit;
	}

	public Team() {
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
	
	public void addStudent(Student student) {
		this.students.add(student);
	}
	
	public void removeStudent(Student student) {
		this.students.remove(student);
	}

}
