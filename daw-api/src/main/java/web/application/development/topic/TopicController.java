package web.application.development.topic;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.application.development.hello.Hello;
import web.application.development.hello.HelloController;

@RestController
public class TopicController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private TopicService topicService;
	
	@RequestMapping(value="/topics", method=RequestMethod.GET) //maps URL /topics to method getAllTopics
	public List<Topic> getAllTopics() {
		return topicService.getAllTopics();
	}
	
	/*@RequestMapping(value="/topics/{id}", method=RequestMethod.GET) //{} tells spring the containing part is a variable
	public Topic getTopic(@PathVariable String id) { //annotation that maps {id} to String id
		return topicService.getTopic(id);
	}*/
	
	@RequestMapping(value="/topics", method=RequestMethod.POST)
	public void addTopic(@RequestBody Topic topic) { //@RequestBody tells spring that the request pay load is going to contain a topics
		topicService.addTopic(topic);
	}
	
	@RequestMapping(value="/topics/{id}", method=RequestMethod.PUT)
	public void updateTopic(@RequestBody Topic topic, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a topics
		topicService.updateTopic(id, topic);
	}
	
	@RequestMapping(value="/topics/{id}", method=RequestMethod.DELETE)
	public void deleteTopic(@PathVariable String id) {
		topicService.deleteTopic(id);
	}
	
	@RequestMapping(value="/topics/{id}", method=RequestMethod.GET)
	public HttpEntity<Topic> getTopic(@PathVariable String id) {
		Topic topic = topicService.getTopic(id);
		
		return new ResponseEntity<Topic>(topic, HttpStatus.OK);
	}
}
