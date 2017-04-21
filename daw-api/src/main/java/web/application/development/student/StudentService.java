package web.application.development.student;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
		return studentRepository.findOne(id);
	}
	
	public Student getStudentByName(String name) {
		//return topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return studentRepository.findByName(name);
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
	
	public Page<Student> findAll(Pageable pageable) {
		return studentRepository.findAll(pageable);
	}
}
