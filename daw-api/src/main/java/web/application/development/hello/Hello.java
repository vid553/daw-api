package web.application.development.hello;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import web.application.development.topic.Topic;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hello extends ResourceSupport {

	private String content;
	
	@JsonCreator
	public Hello(@JsonProperty("content") String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
}
