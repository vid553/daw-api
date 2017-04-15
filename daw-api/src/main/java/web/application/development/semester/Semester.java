package web.application.development.semester;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity 
public class Semester {

	@Id	//primary key
	private String id;
	private String name;
	private String season;
	private String leto;
	

	public Semester(String id, String name, String season, String leto) {
		super();
		this.id = id;
		this.name = name;
		this.season = season;
		this.leto = leto;
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

}
