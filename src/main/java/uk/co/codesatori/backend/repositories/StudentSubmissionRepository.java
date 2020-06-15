package uk.co.codesatori.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import uk.co.codesatori.backend.model.StudentSubmission;

import java.util.List;
import java.util.UUID;

public interface StudentSubmissionRepository extends
    CrudRepository<StudentSubmission, StudentSubmission.StudentSubmissionId> {
  List<StudentSubmission> findByStudentId(UUID studentId);
}
