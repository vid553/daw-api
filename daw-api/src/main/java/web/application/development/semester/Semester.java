package web.application.development.semester;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import web.application.development.predavanje.Predavanje;

@Entity 
public class Semester {

	@Id	//primary key
	private String id;
	private String name;
	private String season;
	private String leto;
	
	@OneToMany(targetEntity = Predavanje.class)
	private List<Predavanje> classes;
	
	//doesnt work if uri contains special characters
	public Semester(String id, String name, String season, String leto) {
		super();
		this.id = id;
		this.name = name;
		this.season = season;
		this.leto = leto;
		classes = new ArrayList<Predavanje>();
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
	
	public List<Predavanje> getPredmeti() {
		return classes;
	}

	public void setPredmeti(List<Predavanje> predmeti) {
		this.classes = predmeti;
	}
	
	public void addPredmet(Predavanje predmet) {
		this.classes.add(predmet);
	}
	
	public void removePredmet(Predavanje predmet) {
		List<Predavanje> predmeti = new ArrayList<Predavanje>();
		for(Predavanje p : this.classes){
		    if(p.getId().equals(predmet.getId())) {
		    	predmeti.add(p);
		    }
		}
		this.classes.removeAll(predmeti);
	}
}
