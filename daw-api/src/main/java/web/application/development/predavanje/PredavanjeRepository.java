package web.application.development.predavanje;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PredavanjeRepository extends PagingAndSortingRepository<Predavanje, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	Predavanje findByIdentifier(String identifier);
	Page<Predavanje> findAll(Pageable pageable);
}
