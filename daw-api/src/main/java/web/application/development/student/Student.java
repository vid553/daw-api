package web.application.development.student;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;

import web.application.development.predavanje.Predavanje;

@Entity 
public class Student{

	@Id	//primary key
	private String id;
	private String name;
	private String email;
	private String number;

	@ManyToMany(targetEntity = Predavanje.class)
	private List<Predavanje> predavanja;

	public Student(String id, String name, String email, String number) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.number = number;
		this.predavanja = new ArrayList<Predavanje>();
	}

	public List<Predavanje> getClasses() {
		return predavanja;
	}

	public void setClasses(List<Predavanje> classes) {
		this.predavanja = classes;
	}

	public Student() {
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
	
	public void enrollIntoClass(Predavanje predavanje) {
		if (!this.predavanja.contains(predavanje)) {
			this.predavanja.add(predavanje);
		}
	}
	
	@PreRemove
	private void removePredavanjeFromStudent() {
	    for (Predavanje p : this.predavanja) {
	        p.getStudents().remove(this);
	    }
	}
}
