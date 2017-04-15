package web.application.development.student;

import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	
}
