package web.application.development.exception;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Error extends ResourceSupport {
	private final String type;
	private final String title;
	private final String detail;
	
	@JsonCreator
	public Error(@JsonProperty("type") String type, @JsonProperty("title") String title, @JsonProperty("detail") String detail) {
		this.type = type;
		this.title = title;
		this.detail = detail;
	}
	
	public String getErrorType() {
		return type;
	}
	
	public String getErrorTitle() {
		return title;
	}
	
	public String getErrorDetail() {
		return detail;
	}
}
