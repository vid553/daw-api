package web.application.development.semester;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import web.application.development.predmet.Predmet;
import web.application.development.student.Student;

@Entity 
public class Semester {

	@Id	//primary key
	private String id;
	private String name;
	private String season;
	private String leto;
	
	@OneToMany
	private List<Predmet> predmeti;
	

	public Semester(String id, String name, String season, String leto) {
		super();
		this.id = id;
		this.name = name;
		this.season = season;
		this.leto = leto;
		predmeti = new ArrayList<Predmet>();
	}
	
	public Semester() {
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
	
	public String getSeason() {
		return season;
	}
	
	public void setSeason(String season) {
		this.season = season;
	}
	
	public String getLeto() {
		return leto;
	}
	
	public void setLeto(String leto) {
		this.leto = leto;
	}
	
	public List<Predmet> getPredmeti() {
		return predmeti;
	}

	public void setPredmeti(List<Predmet> predmeti) {
		this.predmeti = predmeti;
	}
	
	public void addPredmet(Predmet predmet) {
		this.predmeti.add(predmet);
	}
	
	public void removePredmet(Predmet predmet) {
		List<Predmet> predmeti = new ArrayList<Predmet>();
		for(Predmet p : this.predmeti){
		    if(p.getId().equals(predmet.getId())) {
		    	predmeti.add(p);
		    }
		}
		this.predmeti.removeAll(predmeti);
	}
}
