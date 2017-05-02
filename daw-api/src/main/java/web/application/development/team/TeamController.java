package web.application.development.team;

import static web.application.development.team.TeamComparator.*;

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
import web.application.development.student.Student;
import web.application.development.student.StudentService;


@RestController
public class TeamController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing groupService
	private TeamService groupService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private Formatter formatter;
	
	private HttpHeaders sirenHeader = Headers.SirenHeader();
	private HttpHeaders problemHeader = Headers.ProblemHeader();
	
	//works, if non-existing class -> returns 404
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.GET) //maps URL /groups to method getAllGroups
	public ResponseEntity<?> getGroup(@PathVariable String groupId) {
		Team group = groupService.getGroup(groupId);
		if (group != null) {
			try {
				JsonObject object = formatter.ReturnJSON(group);
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
	
	//works, if non-existing class -> returns 404
	@RequestMapping(value="/groups", method=RequestMethod.GET) //maps URL /groups to method getAllGroups
	public ResponseEntity<?> getAllGroups() {
		List<Team> group = groupService.getAllGroups();
		if (group.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			try {
				JsonObject object = formatter.ReturnJSON(group, new Team());
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
	
	//works
	@RequestMapping(value="/groups", method=RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public ResponseEntity<?> addGroup(@RequestBody Team group) { //@RequestBody tells spring that the request pay load is going to contain a course
		groupService.addGroup(group);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//works
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.PUT) //change group info
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public ResponseEntity<?> updateGroup(@RequestBody Team group, @PathVariable String groupId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team temp = groupService.getGroup(groupId);
		List<Student> students = temp.getStudents();
		group.setStudents(students);
		groupService.updateGroup(groupId, group);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.DELETE) //delete group
	@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
	public ResponseEntity<?> deleteGroup(@PathVariable String groupId) {
		groupService.deleteGroup(groupId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/groups/{groupId}/{studentId}", method=RequestMethod.POST) //adds existing student to group, NO BODY on POST
	public ResponseEntity<?> addStudentToGroup(@PathVariable String groupId, @PathVariable String studentId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team group = groupService.getGroup(groupId);
		Student student = studentService.getStudent(studentId);
		List<Predavanje> predavanja = student.getClasses();
		if (predavanja.contains(group.getPredavanje())) {
			List<Student> students = group.getStudents();
			if (students.size() < group.getStudents_limit()) {
				group.addStudent(new Student(studentId, "","",""));
				groupService.addStudentToGroup(groupId, group);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
		        Error error = new Error("http://localhost:8080/error/permision", "The group is full.", "The group has reached its limit of " + group.getStudents_limit() + " students.");
		        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.FORBIDDEN);
			}
		}
		else {
			Error error = new Error("http://localhost:8080/error/permision", "Student not enrolled in the class.", "The requested student is not enrolled in required class.");
	        return new ResponseEntity<Error>(error, problemHeader, HttpStatus.FORBIDDEN);
		}
	}

	//works
	@RequestMapping(value="/groups/{groupId}/{studentId}", method=RequestMethod.DELETE) //removes student from group
	public ResponseEntity<?> removeStudentFromGroup(@PathVariable String groupId, @PathVariable String studentId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team temp = groupService.getGroup(groupId);
		temp.removeStudent(new Student(studentId, "","",""));
		groupService.removeStundentFromGroup(groupId, temp);
		if (temp.getStudents().isEmpty()) {	// if the last student leaves, the group gets deleted
			groupService.deleteGroup(groupId);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//sort parameters are NAME_SORT, ID_SORT
	@RequestMapping(value="/groups/sort/descending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedGroupsDescending(@PathVariable List<String> sortParameter) {
		List<Team> groups = groupService.getAllGroups();
		
		List<TeamComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(TeamComparator.valueOf(s));
		}
		Collections.sort(groups, descending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(groups, new Team());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/groups/sort/ascending/{sortParameter}", method=RequestMethod.GET) //maps URL /students to method getAllStudents
	public ResponseEntity<Entity> getSortedGroupsAscending(@PathVariable List<String> sortParameter) {
		List<Team> groups = groupService.getAllGroups();
		
		List<TeamComparator> comparators = new ArrayList<>();
		for (String s : sortParameter) {
			comparators.add(TeamComparator.valueOf(s));
		}
		Collections.sort(groups, ascending(getComparator(comparators)));
		
		JsonObject object = formatter.ReturnJSON(groups, new Team());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, sirenHeader, HttpStatus.OK);
	}
	
	@RequestMapping(value="/groups/listed/{pageNum}/{sizeNum}", method=RequestMethod.GET)
	public HttpEntity<?> getGroups(@PathVariable int pageNum, @PathVariable int sizeNum) {
		Page<Team> pageGroups = groupService.findAll(new PageRequest(pageNum, sizeNum));
		if (pageGroups.getTotalPages() != 0) {
			try {
				List<Team> groups = pageGroups.getContent();
				JsonObject object = formatter.ReturnJSON(groups, new Team());
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