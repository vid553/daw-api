package web.application.development.team;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sebastian_daschner.siren4javaee.EntityBuilder;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.course.Course;
import web.application.development.student.Student;
import web.application.development.topic.Topic;

@Service
public class TeamService {
	
	@Autowired //injects instance of TopicRepository
	private TeamRepository groupRepository;

	/*public List<Team> getAllGroups(String studentId) {
		List<Team> groups = new ArrayList<>();
		groupRepository.findByStudentId(studentId).forEach(groups::add);
		return groups;
	}*/
	
	public List<Team> getAllGroups() {
		List<Team> groups = new ArrayList<>();
		groupRepository.findAll().forEach(groups::add);
		return groups;
	}
	
	public Team getGroup(String id) {
		//return topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return groupRepository.findOne(id);
	}

	public void addGroup(Team group) {
		groupRepository.save(group);
	}
	
	public void updateGroup(String id, Team group) {
		groupRepository.save(group);
	}

	public void deleteGroup(String id) {
		groupRepository.delete(id);
	}
	
	public void addStudentToGroup(String id, Team group) {
		groupRepository.save(group);
	}
	
	public void removeStundentFromGroup(String id, Team group) {
		groupRepository.save(group);
	}
	
}
