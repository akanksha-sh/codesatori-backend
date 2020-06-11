package uk.co.codesatori.backend.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import uk.co.codesatori.backend.model.Assignment;

public interface AssignmentRepository extends CrudRepository<Assignment, UUID> {

}
