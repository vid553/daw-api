package web.application.development.student;

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

import web.application.development.exception.Error;
import web.application.development.exception.ErrorLog;
import web.application.development.formatter.Formatter;
import web.application.development.headers.Headers;
import web.application.development.predavanje.Predavanje;
import web.application.development.predavanje.PredavanjeService;
import web.application.development.teacher.Teacher;

import static web.application.development.student.StudentComparator.*;

@RestController
public class StudentController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing studentService
	private StudentService studentService;
	@Autowired 
	private PredavanjeService predmetService;
	@Autowired
	private Formatter formatter;
	
	private HttpHeaders sirenHeader = Headers.SirenHeader();
	private HttpHeaders problemHeader = Headers.ProblemHeader();
	
	//works empty or with added entities, if non-existing class -> returns 404
	@RequestMapping(value="/students", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<?> getAllStudents() {
		List<Student> students = studentService.getAllStudents();
		if (students.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			try {
				JsonObject object = formatter.ReturnJSON(students, new Student());
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
	
	@RequestMapping(value="/students/listed/{pageNum}/{sizeNum}", method=RequestMethod.GET) 
	public ResponseEntity<?> getAllStudentsPages(@PathVariable int pageNum, @PathVariable int sizeNum) {				
		Page<Student> pageStudents = studentService.findAll(new PageRequest(pageNum, sizeNum));
		if (pageStudents.getTotalPages() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			try {
				List<Student> students = pageStudents.getContent();
				JsonObject object = formatter.ReturnJSON(students, new Student());
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
	
	//works if student exists, if non-existing class -> returns 404
	@RequestMapping(value="/students/{id}", method=RequestMethod.GET)
	public HttpEntity<?> getStudent(@PathVariable String id) {
		Student student = studentService.getStudent(id);
		if (student != null) {
			try {
				JsonObject object = formatter.ReturnJSON(student);
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

	//works
	@RequestMapping(value="/students", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN')")	// only administrators can create users  
	public ResponseEntity<?> addStudent(@RequestBody Student student) { //@RequestBody tells spring that the request pay load is going to contain a user
		studentService.addStudent(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/students/{id}", method=RequestMethod.PUT)
	@PreAuthorize("hasRole('ADMIN')")	// only admins can edit users 
	public ResponseEntity<?> updateStudent(@RequestBody Student student, @PathVariable String id) { 
		studentService.updateStudent(id, student);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/students/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")	// only admins can delete users
	public ResponseEntity<?> deleteStudent(@PathVariable String id) {
		studentService.deleteStudent(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/students/{id}/{classId}", method=RequestMethod.POST)
	public ResponseEntity<?> enrollStudentToClass(@PathVariable String id, @PathVariable String classId) {
		Predavanje predmet = predmetService.getPredavanje(classId);
		if (predmet.getEnrolment()) {
			predmet.enrollIntoClass(new Student(id, "","",""));
			predmetService.enrollStudentIntoClass(classId, predmet);
			Student student = studentService.getStudent(id);
			student.enrollIntoClass(new Predavanje(classId, "", false));
			studentService.enrollStudentIntoClass(id, student);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		else {
			if (enrollAuthenticated()) {
				predmet.enrollIntoClass(new Student(id, "","",""));
				predmetService.enrollStudentIntoClass(classId, predmet);
				Student student = studentService.getStudent(id);
				student.enrollIntoClass(new Predavanje(classId, "", false));
				studentService.enrollStudentIntoClass(id, student);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				Error error = new Error("http://localhost:8080/error/permision", "Can't join the class.", "This class does not have auto enrollment enabled. Only teachers can add students.");
		        HttpHeaders headers = new HttpHeaders();
		        headers.add("Content-Type", "application/problem+json");
		        headers.add("Content-Language", "en");
		        return new ResponseEntity<Error>(error, headers, HttpStatus.FORBIDDEN);
			}
		}
		
	}
	
	@RequestMapping(value="/students/{id}/{classId}", method=RequestMethod.DELETE)
	public ResponseEntity<?> removeStudentFromClass(@PathVariable String id, @PathVariable String classId) {
		Student student = studentService.getStudent(id);
		student.removeFromClass(new Predavanje(classId, "", false));
		studentService.updateStudent(id, student);
		
		Predavanje predmet = predmetService.getPredavanje(classId);
		predmet.removeStudent(new Student(id, "", "",""));
		predmetService.updatePredavanje(classId, predmet);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public Boolean enrollAuthenticated() {
		return true;
	}
	
	//sort parameters are NAME_SORT, ID_SORT, NUMBER_SORT, EMAIL_SORT
	@RequestMapping(value="/students/sort/descending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedStudentsDescending(@PathVariable List<String> sortParameter) {
		List<Student> students = studentService.getAllStudents();
		
		List<StudentComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(StudentComparator.valueOf(s));
		}
		Collections.sort(students, descending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(students, new Student());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/students/sort/ascending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedStudentsAscendign(@PathVariable List<String> sortParameter) {
		List<Student> students = studentService.getAllStudents();
		
		List<StudentComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(StudentComparator.valueOf(s));
		}
		Collections.sort(students, ascending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(students, new Student());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
}