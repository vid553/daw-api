package web.application.development.predmet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import web.application.development.team.Team;

@Entity 
public class Predmet {

	@Id	//primary key
	private String id;
	private String identifier;
	private Boolean auto_enrolment;
	
	@OneToMany
	private List<Team> teams;

	public Predmet(String id, String identifier, Boolean auto_enrolment) {
		super();
		this.id = id;
		this.identifier = identifier;
		this.auto_enrolment = auto_enrolment;
		teams = new ArrayList<Team>();
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
}
