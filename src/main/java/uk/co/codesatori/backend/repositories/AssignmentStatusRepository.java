package uk.co.codesatori.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import uk.co.codesatori.backend.model.AssignmentStatus;

public interface AssignmentStatusRepository extends
    CrudRepository<AssignmentStatus, AssignmentStatus.AssignmentStatusId> {

}
