package web.application.development.student;

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

@Service
public class StudentService {
	
	@Autowired //injects instance of studentRepository
	private StudentRepository studentRepository;

	public List<Student> getAllStudents() {
		List<Student> students = new ArrayList<>();
		studentRepository.findAll().forEach(students::add);
		return students;
	}
	
	public Student getStudent(String id) {
		//return topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return studentRepository.findOne(id);
	}

	public void addStudent(Student person) {
		studentRepository.save(person);
	}
	
	public void updateStudent(String id, Student person) {
		studentRepository.save(person);
	}

	public void deleteStudent(String id) {
		studentRepository.delete(id);
	}
	
	public void enrollStudentIntoClass(String id, Student student) {
		studentRepository.save(student);
	}
	
}
