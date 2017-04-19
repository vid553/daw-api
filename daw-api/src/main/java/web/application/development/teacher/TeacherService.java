package web.application.development.teacher;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public void assignTeacherToClass(String id, Teacher teacher) {
		teacherRepository.save(teacher);
	}
	
	public Teacher getTeacherByName(String name) {
		return teacherRepository.findByName(name);
	}
}
