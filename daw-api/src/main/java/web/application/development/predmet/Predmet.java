package web.application.development.predmet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity 
public class Predmet {

	@Id	//primary key
	private String id;
	private String identifier;
	private Boolean auto_enrolment;
	

	public Predmet(String id, String identifier, Boolean auto_enrolment) {
		super();
		this.id = id;
		this.identifier = identifier;
		this.auto_enrolment = auto_enrolment;
	}
	
	public Predmet() {
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

}
