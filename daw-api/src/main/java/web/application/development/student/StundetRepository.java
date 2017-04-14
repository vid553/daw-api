package web.application.development.student;

import org.springframework.data.repository.CrudRepository;

public interface StundetRepository extends CrudRepository<Student, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	
}
