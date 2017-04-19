package web.application.development.teacher;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import web.application.development.course.Course;
import web.application.development.predavanje.Predavanje;

@Entity 
public class Teacher{

	@Id	//primary key
	private String id;
	private String name;
	private String email;
	private String number;
	private Boolean admin;
	
	@OneToMany(targetEntity = Course.class)
	private List<Course> courses;

	@ManyToMany(targetEntity = Predavanje.class)
	private List<Predavanje> predavanja;

	public Teacher(String id, String name, String email, String number, Boolean admin) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.number = number;
		this.admin = admin;
		courses = new ArrayList<Course>();
		predavanja = new ArrayList<Predavanje>();
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
	
	public void assignTeacherToClass(Predavanje predmet) {
		if (!this.predavanja.contains(predmet)) {
			this.predavanja.add(predmet);
		}
	}

	public List<Predavanje> getPredavanja() {
		return predavanja;
	}

	public void setPredavanja(List<Predavanje> predmeti) {
		this.predavanja = predmeti;
	}
	
	@PreRemove
	private void removePredavanjeFromTeacher() {
	    for (Predavanje p : this.predavanja) {
	        p.getTeachers().remove(this);
	    }
	}
}
