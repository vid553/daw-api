package web.application.development.search;

import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian_daschner.siren4javaee.Entity;
import com.sebastian_daschner.siren4javaee.EntityReader;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.develeopment.headers.Headers;
import web.application.development.course.Course;
import web.application.development.course.CourseService;
import web.application.development.exception.Error;
import web.application.development.formatter.Formatter;
import web.application.development.predavanje.Predavanje;
import web.application.development.predavanje.PredavanjeService;
import web.application.development.semester.Semester;
import web.application.development.semester.SemesterService;
import web.application.development.student.Student;
import web.application.development.student.StudentService;
import web.application.development.teacher.Teacher;
import web.application.development.teacher.TeacherService;
import web.application.development.team.Team;
import web.application.development.team.TeamService;

@RestController
public class SearchController {
	
	@Autowired
	private TeacherService teacherService;
	@Autowired 
	private StudentService studentService;
	@Autowired 
	private SemesterService semesterService;
	@Autowired 
	private PredavanjeService predavanjeService;
	@Autowired 
	private CourseService courseService;
	@Autowired 
	private TeamService teamService;
	@Autowired
	private Formatter formatter;
	
	private HttpHeaders sirenHeader = Headers.SirenHeader();
	private HttpHeaders problemHeader = Headers.ProblemHeader();
	
	//works if student exists, if non-existing class -> returns 404
	@RequestMapping(value="search/students/{name}", method=RequestMethod.GET)
	public HttpEntity<?> getStudentByName(@PathVariable String name) {
		Student student = studentService.getStudentByName(name);
		if (student != null) {
			try {
				JsonObject object = formatter.ReturnJSON(student);
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), errorsInfo[1]);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/search/teachers/{name}", method=RequestMethod.GET)
	public HttpEntity<?> getTeacherByName(@PathVariable String name) {
		Teacher teacher = teacherService.getTeacherByName(name);
		if (teacher != null) {
			try {
				JsonObject object = formatter.ReturnJSON(teacher);
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
	
	@RequestMapping(value="/search/semesters/{name}", method=RequestMethod.GET)
	public HttpEntity<?> getSemesterByName(@PathVariable String name) {
		Semester semester = semesterService.getSemesterByName(name);
		if (semester != null) {
			try {
				JsonObject object = formatter.ReturnJSON(semester);
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), errorsInfo[1]);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/search/classes/{identifier}", method=RequestMethod.GET)
	public HttpEntity<?> getPredavanjeByIdentifier(@PathVariable String identifier) {
		Predavanje predmet = predavanjeService.getPredavanjeByIdentifier(identifier);
		if (predmet != null) {
			try {
				JsonObject object = formatter.ReturnJSON(predmet);
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
		
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/search/groups/{name}", method=RequestMethod.GET) //maps URL /groups to method getAllGroups
	public ResponseEntity<?> getGroupByName(@PathVariable String name) {
		Team group = teamService.getGroupByName(name);
		if (group != null) {
			try {
				JsonObject object = formatter.ReturnJSON(group);
				EntityReader entityReader = Siren.createEntityReader();
				Entity entity = entityReader.read(object);
				return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
			}
			catch (Exception ex) {
				String errorMessage = ex + "";
				String[] errorsInfo = errorMessage.split(": ");
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), errorsInfo[1]);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/search/courses/{name}", method=RequestMethod.GET)
	public HttpEntity<?> getCourseByName(@PathVariable String name) {
		Course course = courseService.getCourseByName(name);
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
		        Error error = new Error("about:blank", errorsInfo[0].substring(errorsInfo[0].lastIndexOf(".")+1), errorsInfo[1]);
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
