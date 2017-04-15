package web.application.development.teacher;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sebastian_daschner.siren4javaee.EntityBuilder;
import com.sebastian_daschner.siren4javaee.Siren;

import web.application.development.team.Team;

@Service
public class TeacherService {
	
	@Autowired //injects instance of TopicRepository
	private TeacherRepository teacherRepository;

	public List<Teacher> getAllTeachers() {
		List<Teacher> teachers = new ArrayList<>();
		teacherRepository.findAll().forEach(teachers::add);
		return teachers;
	}
	
	public Teacher getTeacher(String id) {
		//return topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return teacherRepository.findOne(id);
	}

	public void addTeacher(Teacher teacher) {
		teacherRepository.save(teacher);
	}
	
	public void updateTeacher(String id, Teacher teacher) {
		teacherRepository.save(teacher);
	}

	public void deleteTeacher(String id) {
		teacherRepository.delete(id);
	}
	
	public void addCourseToTeacher(String id, Teacher teacher) {
		teacherRepository.save(teacher);
	}
	
	public void removeCourseFromTeacher(String id, Teacher teacher) {
		teacherRepository.save(teacher);
	}
	
}
