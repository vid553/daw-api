package web.application.development.team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import web.application.development.student.Student;


@RestController
public class TeamController {
	
	@Autowired //marks this as something that needs dependency injection, injects existing groupService
	private TeamService groupService;
	
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.GET) //maps URL /groups to method getAllGroups
	public Team getGroup(@PathVariable String groupId) {
		return groupService.getGroup(groupId);
	}
	
	@RequestMapping(value="/groups", method=RequestMethod.GET) //maps URL /groups to method getAllGroups
	public List<Team> getAllGroups() {
		return groupService.getAllGroups();
	}
	
	@RequestMapping(value="/groups", method=RequestMethod.POST)
	public void addGroup(@RequestBody Team group) { //@RequestBody tells spring that the request pay load is going to contain a course
		groupService.addGroup(group);
	}
	
	@RequestMapping(value="/groups/{groupId}/{studentId}", method=RequestMethod.POST) //adds existing student to group, NO BODY on POST
	public void addStudentToGroup(@PathVariable String groupId, @PathVariable String studentId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team group = groupService.getGroup(groupId);
		group.addStudent(new Student(studentId, "","",""));
		groupService.addStudentToGroup(groupId, group);
	}

	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.PUT) //change group info
	public void updateGroup(@RequestBody Team group, @PathVariable String groupId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team temp = groupService.getGroup(groupId);
		List<Student> students = temp.getStudents();
		group.setStudents(students);
		groupService.updateGroup(groupId, group);
	}
	
	@RequestMapping(value="/groups/{groupId}", method=RequestMethod.DELETE) //delete group
	public void deleteGroup(@PathVariable String groupId) {
		groupService.deleteGroup(groupId);
	}
	
	//deletes student from group (NOT WORKING)
	@RequestMapping(value="/groups/{groupId}/{studentId}", method=RequestMethod.DELETE) //changes student in group
	public void remveStudentFromGroup(@PathVariable String groupId, @PathVariable String studentId) { //@RequestBody tells spring that the request pay load is going to contain a topics
		Team temp = groupService.getGroup(groupId);
		temp.removeStudent(new Student(studentId, "","",""));
		groupService.removeStundentFromGroup(groupId, temp);
	}

}
