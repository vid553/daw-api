package web.application.development.semester;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SemesterRepository extends PagingAndSortingRepository<Semester, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>
	Semester findByName(String name);
	Page<Semester> findAll(Pageable pageable);
}
