package web.application.development.course;

import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian_daschner.siren4javaee.Entity;
import com.sebastian_daschner.siren4javaee.EntityReader;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.formatter.Formatter;
import web.application.development.predmet.Predmet;
import web.application.development.teacher.Teacher;

@RestController
public class CourseController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private CourseService courseService;
	@Autowired
	private Formatter formatter;
	
	@RequestMapping(value="/courses", method=RequestMethod.GET) //maps URL /courses to method getAllCourses
	public ResponseEntity<Entity> getAllCourses() {
		JsonObject object = formatter.ReturnJSON(courseService.getAllCourses(), new Course());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/courses/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getCourse(@PathVariable String id) {
		Course course = courseService.getCourse(id);
		JsonObject object = formatter.ReturnJSON(course);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/courses", method=RequestMethod.POST)
	public void addCourse(@RequestBody Course course) { //@RequestBody tells spring that the request pay load is going to contain a user
		courseService.addCourse(course);
	}

	
	@RequestMapping(value="/courses/{courseId}/{classId}", method=RequestMethod.POST) //adds existing class to a course
	public void addClassToCourse(@PathVariable String classId, @PathVariable String courseId) {
		Course course = courseService.getCourse(courseId);
		course.addClass(new Predmet(classId, "", false));
		courseService.addClassToCourse(courseId, course);
	}
	
	@RequestMapping(value="/courses/{courseId}", method=RequestMethod.PUT)
	public void updateCourse(@RequestBody Course course, @PathVariable String courseId) {
		Course temp = courseService.getCourse(courseId);
		List<Predmet> classes = temp.getClasses();
		course.setClasses(classes);
		courseService.updateCourse(courseId, course);
	}
	
	@RequestMapping(value="/courses/{id}", method=RequestMethod.DELETE)
	public void deleteCourse(@PathVariable String id) {
		courseService.deleteCourse(id);
	}
	
	@RequestMapping(value="/courses/{courseId}/{classId}", method=RequestMethod.DELETE) //removes course from teacher, body has to have course ID
	public void remveStudentFromGroup(@PathVariable String courseId, @PathVariable String classId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Course temp = courseService.getCourse(courseId);
		temp.removeClass(new Predmet(classId, "", false));
		courseService.removeClassFromCourse(courseId, temp);
	}
}
