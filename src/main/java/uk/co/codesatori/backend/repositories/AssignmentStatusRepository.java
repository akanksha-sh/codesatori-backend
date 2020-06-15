package uk.co.codesatori.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import uk.co.codesatori.backend.model.AssignmentStatus;

import java.util.List;
import java.util.UUID;

public interface AssignmentStatusRepository extends
    CrudRepository<AssignmentStatus, AssignmentStatus.AssignmentStatusId> {
  List<AssignmentStatus> findByClassId(UUID classId);
}
