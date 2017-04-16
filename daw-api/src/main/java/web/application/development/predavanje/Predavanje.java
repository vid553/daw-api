package web.application.development.predavanje;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import web.application.development.student.Student;
import web.application.development.teacher.Teacher;
import web.application.development.team.Team;

@Entity
public class Predavanje {

	@Id	//primary key
	private String id;
	private String identifier;
	private Boolean auto_enrolment; //JSON takes enrolment as argument, JSON representation is enrolment_auto
	
	@OneToMany
	private List<Team> teams;
	
	@ManyToMany
	private List<Student> students;
	
	@ManyToMany
	private List<Teacher> teachers;

	public Predavanje(String id, String identifier, Boolean auto_enrolment) {
		super();
		this.id = id;
		this.identifier = identifier;
		this.auto_enrolment = auto_enrolment;
		teams = new ArrayList<Team>();
		students = new ArrayList<Student>();
	}
	
	public Predavanje() {
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public Boolean getEnrolment() {
		return auto_enrolment;
	}
	
	public void setEnrolment(Boolean auto_enrolment) {
		this.auto_enrolment = auto_enrolment;
	}
	
	public List<Team> getTeams() {
		return teams;
	}
	
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public void addTeam(Team team) {
		this.teams.add(team);
	}
	
	public void removeTeam(Team team) {
		List<Team> teams = new ArrayList<Team>();
		for (Team t : this.teams) {
			if (t.getId().equals(team.getId())) {
				teams.add(t);
			}
		}
		this.teams.removeAll(teams);
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}
	
	public void assignTeacherToClass(Teacher teacher) {
		if (!this.teachers.contains(teacher)) {
			this.teachers.add(teacher);
		}
	}
	
	public void enrollIntoClass(Student student) {
		if (!this.students.contains(student)) {
			this.students.add(student);
		}
	}
	
}
