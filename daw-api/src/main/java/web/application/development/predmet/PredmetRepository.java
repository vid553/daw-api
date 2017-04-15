package web.application.development.predmet;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PredmetRepository extends CrudRepository<Predmet, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	
}
