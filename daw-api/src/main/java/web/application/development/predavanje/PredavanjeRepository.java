package web.application.development.predavanje;

import org.springframework.data.repository.CrudRepository;

public interface PredavanjeRepository extends CrudRepository<Predavanje, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	Predavanje findByIdentifier(String identifier);
}
