package web.application.development.course;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import web.application.development.predavanje.Predavanje;
import web.application.development.teacher.Teacher;

@Entity 
public class Course {

	@Id	//primary key
	private String id;
	private String name;
	private String acronim;
	
	@OneToMany(targetEntity = Predavanje.class)
	private List<Predavanje> classes;
	
	@ManyToOne
	private Teacher teacher;

	public Course(String id, String name, String acronim) {
		super();
		this.id = id;
		this.name = name;
		this.acronim = acronim;
		classes = new ArrayList<Predavanje>();
	}
	
	public List<Predavanje> getClasses() {
		return classes;
	}

	public void setClasses(List<Predavanje> classes) {
		this.classes = classes;
	}

	public Course() {
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
	
	public String getAcronim() {
		return acronim;
	}
	
	public void setAcronim(String acronim) {
		this.acronim = acronim;
	}
	
	public void addClass(Predavanje predmet) {
		this.classes.add(predmet);
	}
	
	public void removeClass(Predavanje predmet) {
		List<Predavanje> classes = new ArrayList<Predavanje>();
		for(Predavanje p : this.classes){
		    if(p.getId().equals(predmet.getId())) {
		    	classes.add(p);
		    }
		}
		this.classes.removeAll(classes);
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	@PreRemove
	private void removeTeacher() {
	    
	    if (this.teacher != null) {this.teacher.getCourses().remove(this);}
	    
	    if (this.classes != null ) {
	    	for (Predavanje p : this.classes) {
	    		if(p.getCourse() == this) {
	    			p.setCourse(null);
	    		}
	    	}
	    }
	}
}
