package web.application.development.hello;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {

	private static String TEMPLATE = "Hello, %s";
	
	@RequestMapping("/hello")
	public HttpEntity<Hello> hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
		Hello hello = new Hello(String.format(TEMPLATE, name));
		hello.add(linkTo(methodOn(HelloController.class).hello(name)).withSelfRel());
		
		return new ResponseEntity<Hello>(hello, HttpStatus.OK);
	}
}
