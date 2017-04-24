package web.application.development.teacher;

import static web.application.development.teacher.TeacherComparator.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian_daschner.siren4javaee.Entity;
import com.sebastian_daschner.siren4javaee.EntityReader;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.course.Course;
import web.application.development.course.CourseService;
import web.application.development.exception.Error;
import web.application.development.exception.ErrorLog;
import web.application.development.formatter.Formatter;
import web.application.development.headers.Headers;
import web.application.development.predavanje.Predavanje;
import web.application.development.predavanje.PredavanjeService;

@RestController
public class TeacherController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing topicService
	private TeacherService teacherService;
	@Autowired 
	private PredavanjeService predmetService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private Formatter formatter;
	
	private HttpHeaders sirenHeader = Headers.SirenHeader();
	private HttpHeaders problemHeader = Headers.ProblemHeader();
	
	//works if teacher exists, if non-existing class -> returns 404
	@RequestMapping(value="/teachers", method=RequestMethod.GET) //maps URL /teachers to method getAllTeachers
	public ResponseEntity<?> getAllTeachers() {
		List<Teacher> teachers = teacherService.getAllTeachers();
		if (teachers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			try {
				JsonObject object = formatter.ReturnJSON(teachers, new Teacher());
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String timeStamp = new ErrorLog().WriteErorLog(ex);
		        Error error = new Error("http://localhost:8080/error/server", "Internal server error", "Error ID: " + timeStamp);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	//works if teacher exists, if non-existing class -> returns 404
	@RequestMapping(value="/teachers/{teacherId}", method=RequestMethod.GET)
	public HttpEntity<?> getTeacher(@PathVariable String teacherId) {
		Teacher teacher = teacherService.getTeacher(teacherId);
		if (teacher != null) {
			try {
				JsonObject object = formatter.ReturnJSON(teacher);
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String timeStamp = new ErrorLog().WriteErorLog(ex);
		        Error error = new Error("http://localhost:8080/error/server", "Internal server error", "Error ID: " + timeStamp);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/teachers", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addUser(@RequestBody Teacher teacher) { //@RequestBody tells spring that the request pay load is going to contain a user
		teacherService.addTeacher(teacher);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/{teacherId}/{courseId}", method=RequestMethod.POST) //adds existing teacher to group, NO BODY on POST
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addStudentToGroup(@PathVariable String teacherId, @PathVariable String courseId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Teacher teacher = teacherService.getTeacher(teacherId);
		teacher.addCourse(new Course(courseId, "",""));
		teacherService.addCourseToTeacher(teacherId, teacher);
		
		Course course = courseService.getCourse(courseId);
		course.setTeacher(teacher);
		courseService.updateCourse(courseId, course);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/{teacherId}/classes/{classId}", method=RequestMethod.POST) //adds existing teacher to group, NO BODY on POST
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> assignTeacherToClass(@PathVariable String teacherId, @PathVariable String classId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Teacher teacher = teacherService.getTeacher(teacherId);
		teacher.assignTeacherToClass(new Predavanje(classId, "",false));
		teacherService.assignTeacherToClass(teacherId, teacher);
		
		Predavanje predmet = predmetService.getPredavanje(classId);
		predmet.assignTeacherToClass(new Teacher(teacherId, "", "", "", false));
		predmetService.assignTeacherToClass(classId, predmet);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value="/teachers/{teacherId}", method=RequestMethod.PUT)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateUser(@RequestBody Teacher teacher, @PathVariable String teacherId) { //@RequestBody tells spring that the request pay load is going to contain a user
		Teacher temp = teacherService.getTeacher(teacherId);
		List<Course> courses = temp.getCourses();
		teacher.setCourses(courses);
		teacherService.updateTeacher(teacherId, teacher);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/{teacherId}", method=RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable String teacherId) {
		teacherService.deleteTeacher(teacherId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/{teacherId}/{courseId}", method=RequestMethod.DELETE) //removes course from teacher, body has to have course ID
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public ResponseEntity<?> remveStudentFromGroup(@PathVariable String courseId, @PathVariable String teacherId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Teacher temp = teacherService.getTeacher(teacherId);
		//Course course = new Course(courseId, "","");
		temp.removeCourse(new Course(courseId, "",""));
		teacherService.removeCourseFromTeacher(teacherId, temp);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//sort parameters are NAME_SORT, ID_SORT, NUMBER_SORT, EMAIL_SORT
	@RequestMapping(value="/teachers/sort/descending/{sortParameter}", method=RequestMethod.GET) //maps URL /teachers to method getAllStudents
	public ResponseEntity<Entity> getSortedTeachersDescending(@PathVariable List<String> sortParameter) {
		List<Teacher> teachers = teacherService.getAllTeachers();
		
		List<TeacherComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(TeacherComparator.valueOf(s));
		}
		Collections.sort(teachers, descending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(teachers, new Teacher());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
	
	//sort parameters are NAME_SORT, ID_SORT, NUMBER_SORT, EMAIL_SORT
	@RequestMapping(value="/teachers/sort/ascending/{sortParameter}", method=RequestMethod.GET) //maps URL /teachers to method getAllStudents
	public ResponseEntity<Entity> getSortedTeachersAscending(@PathVariable List<String> sortParameter) {
		List<Teacher> teachers = teacherService.getAllTeachers();
		
		List<TeacherComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(TeacherComparator.valueOf(s));
		}
		Collections.sort(teachers, ascending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(teachers, new Teacher());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/listed/{pageNum}/{sizeNum}", method=RequestMethod.GET)
	public HttpEntity<?> getTeachers(@PathVariable int pageNum, @PathVariable int sizeNum) {
		Page<Teacher> pageTeachers = teacherService.findAll(new PageRequest(pageNum, sizeNum));
		if (pageTeachers.getTotalPages() != 0) {
			try {
				List<Teacher> teachers = pageTeachers.getContent();
				JsonObject object = formatter.ReturnJSON(teachers, new Teacher());
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String timeStamp = new ErrorLog().WriteErorLog(ex);
		        Error error = new Error("http://localhost:8080/error/server", "Internal server error", "Error ID: " + timeStamp);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
