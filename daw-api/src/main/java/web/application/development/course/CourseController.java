package web.application.development.course;

import static web.application.development.course.CourseComparator.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

import web.application.develeopment.headers.Headers;
import web.application.development.exception.Error;
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
	
	private HttpHeaders sirenHeader = Headers.SirenHeader();
	private HttpHeaders problemHeader = Headers.ProblemHeader();
	
	//works, if non-existing class -> returns 404
	@RequestMapping(value="/courses", method=RequestMethod.GET, produces="application/vnd.siren+json") //maps URL /courses to method getAllCourses
	public ResponseEntity<?> getAllCourses() {
		List<Course> courses = courseService.getAllCourses();
		if (courses.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			try {
				JsonObject object = formatter.ReturnJSON(courses, new Course());
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
				String detail;
				if (errorsInfo.length > 1) {
					detail = errorsInfo[1];
				}
				else {
					detail = "No aditional information available.";
				}
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), detail);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	//works if course exists, if non-existing class -> returns 404
	@RequestMapping(value="/courses/{id}", method=RequestMethod.GET)
	public HttpEntity<?> getCourse(@PathVariable String id) {
		Course course = courseService.getCourse(id);
		if (course != null) {
			try {
				JsonObject object = formatter.ReturnJSON(course);
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
				String detail;
				if (errorsInfo.length > 1) {
					detail = errorsInfo[1];
				}
				else {
					detail = "No aditional information available.";
				}
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), detail);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//works
	@RequestMapping(value="/courses", method=RequestMethod.POST)
	public ResponseEntity<?> addCourse(@RequestBody Course course) { //@RequestBody tells spring that the request pay load is going to contain a user
		courseService.addCourse(course);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value="/courses/{courseId}/{classId}", method=RequestMethod.POST) //adds existing class to a course
	public ResponseEntity<?> addClassToCourse(@PathVariable String classId, @PathVariable String courseId) {
		Course course = courseService.getCourse(courseId);
		course.addClass(new Predavanje(classId, "", false));
		courseService.addClassToCourse(courseId, course);
		
		Predavanje predavanje = predavanjeService.getPredavanje(classId);
		predavanje.setCourse(course);
		predavanjeService.updatePredavanje(classId, predavanje);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/courses/{courseId}", method=RequestMethod.PUT)
	public ResponseEntity<?> updateCourse(@RequestBody Course course, @PathVariable String courseId) {
		Course temp = courseService.getCourse(courseId);
		List<Predavanje> classes = temp.getClasses();
		course.setClasses(classes);
		courseService.updateCourse(courseId, course);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/courses/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteCourse(@PathVariable String id) {
		courseService.deleteCourse(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/courses/{courseId}/{classId}", method=RequestMethod.DELETE) //removes course from teacher, body has to have course ID
	public ResponseEntity<?> remveStudentFromGroup(@PathVariable String courseId, @PathVariable String classId) {
		Course temp = courseService.getCourse(courseId);
		temp.removeClass(new Predavanje(classId, "", false));
		courseService.removeClassFromCourse(courseId, temp);
		return new ResponseEntity<>(HttpStatus.OK);
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
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
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
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
}
