package web.application.development.semester;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SemesterRepository extends CrudRepository<Semester, String> { //implements methods common for all repositories (i.e. getEntity, deleteEntity, etc), <type of entity, type of ID>

}
