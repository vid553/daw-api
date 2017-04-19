package web.application.development.course;

import static web.application.development.course.CourseComparator.*;

import java.util.ArrayList;
import java.util.Collections;
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
import web.application.development.predavanje.Predavanje;
import web.application.development.predavanje.PredavanjeService;

@RestController
public class CourseController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private CourseService courseService;
	@Autowired
	private PredavanjeService predavanjeService;
	@Autowired
	private Formatter formatter;
	
	//works
	@RequestMapping(value="/courses", method=RequestMethod.GET) //maps URL /courses to method getAllCourses
	public ResponseEntity<Entity> getAllCourses() {
		JsonObject object = formatter.ReturnJSON(courseService.getAllCourses(), new Course());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	//works if course exists, TODO: handle non-existing course, returns 500, should return 404
	@RequestMapping(value="/courses/{id}", method=RequestMethod.GET)
	public HttpEntity<Entity> getCourse(@PathVariable String id) {
		Course course = courseService.getCourse(id);
		JsonObject object = formatter.ReturnJSON(course);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/courses", method=RequestMethod.POST)
	public void addCourse(@RequestBody Course course) { //@RequestBody tells spring that the request pay load is going to contain a user
		courseService.addCourse(course);
	}

	
	@RequestMapping(value="/courses/{courseId}/{classId}", method=RequestMethod.POST) //adds existing class to a course
	public void addClassToCourse(@PathVariable String classId, @PathVariable String courseId) {
		Course course = courseService.getCourse(courseId);
		course.addClass(new Predavanje(classId, "", false));
		courseService.addClassToCourse(courseId, course);
		
		Predavanje predavanje = predavanjeService.getPredavanje(classId);
		predavanje.setCourse(course);
		predavanjeService.updatePredavanje(classId, predavanje);
	}
	
	@RequestMapping(value="/courses/{courseId}", method=RequestMethod.PUT)
	public void updateCourse(@RequestBody Course course, @PathVariable String courseId) {
		Course temp = courseService.getCourse(courseId);
		List<Predavanje> classes = temp.getClasses();
		course.setClasses(classes);
		courseService.updateCourse(courseId, course);
	}
	
	//works
	@RequestMapping(value="/courses/{id}", method=RequestMethod.DELETE)
	public void deleteCourse(@PathVariable String id) {
		courseService.deleteCourse(id);
	}
	
	@RequestMapping(value="/courses/{courseId}/{classId}", method=RequestMethod.DELETE) //removes course from teacher, body has to have course ID
	public void remveStudentFromGroup(@PathVariable String courseId, @PathVariable String classId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Course temp = courseService.getCourse(courseId);
		temp.removeClass(new Predavanje(classId, "", false));
		courseService.removeClassFromCourse(courseId, temp);
	}
	
	//sort parameters are NAME_SORT, ID_SORT, ACRONIM_SORT
	@RequestMapping(value="/courses/sort/descending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedCoursesDescending(@PathVariable List<String> sortParameter) {
		List<Course> courses = new ArrayList<>();
		
		List<CourseComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(CourseComparator.valueOf(s));
		}
		Collections.sort(courses, descending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(courses, new Course());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/courses/sort/Ascending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedCoursesAscending(@PathVariable List<String> sortParameter) {
		List<Course> courses = new ArrayList<>();
		
		List<CourseComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(CourseComparator.valueOf(s));
		}
		Collections.sort(courses, ascending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(courses, new Course());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
}
