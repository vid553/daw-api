package web.application.development.team;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import web.application.development.teacher.Teacher;

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
	
	public Team getGroupByName(String name) {
		return groupRepository.findByName(name);
	}
	
	public Page<Team> findAll(Pageable pageable) {
		return groupRepository.findAll(pageable);
	}
	
}
