package web.application.development.topic;

import org.springframework.data.repository.CrudRepository;

public interface TopicRepository extends CrudRepository<Topic, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	
}
