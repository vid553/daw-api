package web.application.development.topic;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity 
public class Topic{

	@Id	//primary key
	private String id;
	private String name;
	private String description;
	private String poskus;
	
	public Topic(String id, String name, String description, String poskus) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.poskus = poskus;
	}
	
	public Topic() {
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getPoskus() {
		return poskus;
	}

	public void setPoskus(String poskus) {
		this.poskus = poskus;
	}
	
}
