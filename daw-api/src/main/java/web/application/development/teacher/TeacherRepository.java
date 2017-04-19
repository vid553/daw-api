package web.application.development.teacher;

import org.springframework.data.repository.CrudRepository;

public interface TeacherRepository extends CrudRepository<Teacher, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	Teacher findByName(String name);
}
