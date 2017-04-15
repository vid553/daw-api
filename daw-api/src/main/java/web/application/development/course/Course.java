package web.application.development.course;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity 
public class Course {

	@Id	//primary key
	private String id;
	private String name;
	private String acronim;
	

	public Course(String id, String name, String acronim) {
		super();
		this.id = id;
		this.name = name;
		this.acronim = acronim;
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

}
