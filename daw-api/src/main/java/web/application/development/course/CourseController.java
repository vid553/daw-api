package web.application.development.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import web.application.development.topic.Topic;

@RestController
public class CourseController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private CourseService courseService;
	
	@RequestMapping(value="/topics/{topicId}/courses", method=RequestMethod.GET) //maps URL /topics to method getAllTopics
	public List<Course> getAllCourses(@PathVariable String topicId) {
		return courseService.getAllCourses(topicId);
	}
	
	@RequestMapping(value="/topics/{topicId}/courses/{courseId}", method=RequestMethod.GET) //{} tells spring the containing part is a variable
	public Course getCourse(@PathVariable String courseId) { //annotation that maps {id} to String id
		return courseService.getCourse(courseId);
	}
	
	@RequestMapping(value="/topics/{topicId}/courses", method=RequestMethod.POST)
	public void addCourse(@RequestBody Course course, @PathVariable String topicId) { //@RequestBody tells spring that the request pay load is going to contain a course
		course.setTopic(new Topic(topicId, "","", ""));
		courseService.addCourse(course);
	}
	
	@RequestMapping(value="/topics/{topicId}/courses/{courseId}", method=RequestMethod.PUT)
	public void updateCourse(@RequestBody Course course, @PathVariable String courseId, @PathVariable String topicId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		course.setTopic(new Topic(topicId, "","", ""));
		courseService.updateCourse(course);
	}
	
	@RequestMapping(value="/topics/{topicId}/courses/{courseId}", method=RequestMethod.DELETE)
	public void deleteCourse(@PathVariable String courseId) {
		courseService.deleteCourse(courseId);
	}
}
