package web.application.development.student;

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

import web.application.development.exception.Error;
import web.application.development.formatter.Formatter;
import web.application.development.predavanje.Predavanje;
import web.application.development.predavanje.PredavanjeService;

@RestController
public class StudentController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing studentService
	private StudentService studentService;
	@Autowired 
	private PredavanjeService predmetService;
	@Autowired
	private Formatter formatter;
	
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
				return new ResponseEntity<Entity>(entity, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), errorsInfo[1]);
		        HttpHeaders headers = new HttpHeaders();
		        headers.add("Content-Type", "application/problem+json");
		        headers.add("Content-Language", "en");
		        return new ResponseEntity<Error>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
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
				return new ResponseEntity<Entity>(entity, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), errorsInfo[1]);
		        HttpHeaders headers = new HttpHeaders();
		        headers.add("Content-Type", "application/problem+json");
		        headers.add("Content-Language", "en");
		        return new ResponseEntity<Error>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//works
	@RequestMapping(value="/students", method=RequestMethod.POST)
	public void addStudent(@RequestBody Student student) { //@RequestBody tells spring that the request pay load is going to contain a user
		studentService.addStudent(student);
	}
	
	//works
	@RequestMapping(value="/students/{id}", method=RequestMethod.PUT)
	public void updateStudent(@RequestBody Student student, @PathVariable String id) { //@RequestBody tells spring that the request pay load is going to contain a user
		studentService.updateStudent(id, student);
	}
	
	//works
	@RequestMapping(value="/students/{id}", method=RequestMethod.DELETE)
	public void deleteStudent(@PathVariable String id) {
		studentService.deleteStudent(id);
	}
	
	//works
	@RequestMapping(value="/students/{id}/{classId}", method=RequestMethod.POST) 
	public void enrollStudentToClass(@PathVariable String id, @PathVariable String classId) {
		Student student = studentService.getStudent(id);
		student.enrollIntoClass(new Predavanje(classId, "", false));
		studentService.enrollStudentIntoClass(id, student);
		
		Predavanje predmet = predmetService.getPredavanje(classId);
		predmet.enrollIntoClass(new Student(id, "","",""));
		predmetService.enrollStudentIntoClass(classId, predmet);
	}

	//when trying to delete student, while student is in group, returns error 500, TODO: handle error
}
