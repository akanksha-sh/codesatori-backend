package uk.co.codesatori.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import uk.co.codesatori.backend.model.StudentSubmission;

public interface StudentSubmissionRepository extends
    CrudRepository<StudentSubmission, StudentSubmission.StudentSubmissionId> {
}
