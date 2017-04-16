package web.application.development.team;

import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
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
import web.application.development.student.Student;


@RestController
public class TeamController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing groupService
	private TeamService groupService;
	@Autowired
	private Formatter formatter;
	
	//works if team exists, TODO: handle non-existing team, returns 500, should return 404
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.GET) //maps URL /groups to method getAllGroups
	public ResponseEntity<Entity> getGroup(@PathVariable String groupId) {
		Team group = groupService.getGroup(groupId);
		JsonObject object = formatter.ReturnJSON(group);
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
		//return groupService.getGroup(groupId);
	}
	
	//works
	@RequestMapping(value="/groups", method=RequestMethod.GET) //maps URL /groups to method getAllGroups
	public ResponseEntity<Entity> getAllGroups() {
		JsonObject object = formatter.ReturnJSON(groupService.getAllGroups(), new Team());
		EntityReader entityReader = Siren.createEntityReader();
		Entity entity = entityReader.read(object);
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}
	
	//works
	@RequestMapping(value="/groups", method=RequestMethod.POST)
	public void addGroup(@RequestBody Team group) { //@RequestBody tells spring that the request pay load is going to contain a course
		groupService.addGroup(group);
	}

	//works
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.PUT) //change group info
	public void updateGroup(@RequestBody Team group, @PathVariable String groupId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team temp = groupService.getGroup(groupId);
		List<Student> students = temp.getStudents();
		group.setStudents(students);
		groupService.updateGroup(groupId, group);
	}
	
	//works
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.DELETE) //delete group
	public void deleteGroup(@PathVariable String groupId) {
		groupService.deleteGroup(groupId);
	}
	
	//works
	@RequestMapping(value="/groups/{groupId}/{studentId}", method=RequestMethod.POST) //adds existing student to group, NO BODY on POST
	public void addStudentToGroup(@PathVariable String groupId, @PathVariable String studentId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team group = groupService.getGroup(groupId);
		group.addStudent(new Student(studentId, "","",""));
		groupService.addStudentToGroup(groupId, group);
	}

	//works
	@RequestMapping(value="/groups/{groupId}/{studentId}", method=RequestMethod.DELETE) //removes student from group
	public void remveStudentFromGroup(@PathVariable String groupId, @PathVariable String studentId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team temp = groupService.getGroup(groupId);
		temp.removeStudent(new Student(studentId, "","",""));
		groupService.removeStundentFromGroup(groupId, temp);
	}

}
