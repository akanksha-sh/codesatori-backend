package uk.co.codesatori.backend.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import uk.co.codesatori.backend.model.ClassOfStudents;

public interface ClassOfStudentsRepository extends CrudRepository<ClassOfStudents, UUID> {

}
