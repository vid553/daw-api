package web.application.development.person;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	
}
