package web.application.development.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
	
	@Autowired //injects instance of TopicRepository
	private CourseRepository courseRepository;

	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<>();
		courseRepository.findAll().forEach(courses::add);
		return courses;
	}
	
	public Course getCourse(String courseId) {
		//return topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return courseRepository.findOne(courseId);
	}

	public void addCourse(Course course) {
		courseRepository.save(course);
	}
	
	public void updateCourse(String id, Course course) {
		courseRepository.save(course);
	}

	public void deleteCourse(String id) {
		courseRepository.delete(id);
	}
	
	public void addClassToCourse(String id, Course course) {
		courseRepository.save(course);
	}
	
	public void removeClassFromCourse(String id, Course course) {
		courseRepository.save(course);
	}
	
	public Course getCourseByName(String name) {
		return courseRepository.findByName(name);
	}
	
	public Page<Course> findAll(Pageable pageable) {
		return courseRepository.findAll(pageable);
	}
}
