package web.application.development.course;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import web.application.development.predmet.Predmet;

@Entity 
public class Course {

	@Id	//primary key
	private String id;
	private String name;
	private String acronim;
	
	@OneToMany
	List<Predmet> classes;

	public Course(String id, String name, String acronim) {
		super();
		this.id = id;
		this.name = name;
		this.acronim = acronim;
		classes = new ArrayList<Predmet>();
	}
	
	public List<Predmet> getClasses() {
		return classes;
	}

	public void setClasses(List<Predmet> classes) {
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
	
	public void addClass(Predmet predmet) {
		this.classes.add(predmet);
	}
	
	public void removeClass(Predmet predmet) {
		List<Predmet> classes = new ArrayList<Predmet>();
		for(Predmet p : this.classes){
		    if(p.getId().equals(predmet.getId())) {
		    	classes.add(p);
		    }
		}
		this.classes.removeAll(classes);
	}

}
