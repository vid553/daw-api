package web.application.development.course;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	
	//public List<Course> findByName(String name); //jpa matches findBy<T> T to variables in class
	//public List<Course> findByDescription(String description);
	
	public List<Course> findByTopicId(String topicId);
}
