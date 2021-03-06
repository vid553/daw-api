package web.application.development.predavanje;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

import web.application.development.course.Course;
import web.application.development.semester.Semester;
import web.application.development.student.Student;
import web.application.development.teacher.Teacher;
import web.application.development.team.Team;

@Entity
public class Predavanje {	// Predavanje is a slovenian word for class ("turma")

	@Id	//primary key
	private String id;
	private String identifier;
	private Boolean auto_enrolment; //JSON takes enrolment as argument, JSON representation is enrolment_auto
	
	@OneToMany(targetEntity = Team.class)
	private List<Team> teams;
	
	@ManyToMany
	private List<Student> students;
	
	@ManyToMany
	private List<Teacher> teachers;
	
	@ManyToOne
	private Course course;
	
	@ManyToOne
	private Semester semester;

	public Predavanje(String id, String identifier, Boolean auto_enrolment) {
		super();
		this.id = id;
		this.identifier = identifier;
		this.auto_enrolment = auto_enrolment;
		teams = new ArrayList<Team>();
		students = new ArrayList<Student>();
		this.course = new Course();
		this.semester = new Semester();
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
	
	public void removeTeacher(Teacher teacher) {
		List<Teacher> teachers = new ArrayList<Teacher>();
		for (Teacher t : this.teachers) {
			if (t.getId().equals(teacher.getId())) {
				teachers.add(t);
			}
		}
		this.teachers.removeAll(teachers);
	}
	
	public void removeStudent(Student student) {
		List<Student> students = new ArrayList<Student>();
		for (Student t : this.students) {
			if (t.getId().equals(student.getId())) {
				students.add(t);
			}
		}
		this.students.removeAll(students);
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
	
	@PreRemove
	private void removePredavanje() {
	    for (Student s : this.students) {
	        s.getClasses().remove(this);
	    }
	    
	    for (Teacher t : this.teachers) {
	        t.getPredavanja().remove(this);
	    }
	    
	    if (this.teams != null ) {
	    	for (Team t : this.teams) {
	    		if(t.getPredavanje() == this) {
	    			t.setPredavanje(null);
	    		}
	    	}
	    }
	    
	    if (this.semester != null) {semester.getPredmeti().remove(this);}
	    
	    if (this.course != null) {course.getClasses().remove(this);}
	}
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

}
