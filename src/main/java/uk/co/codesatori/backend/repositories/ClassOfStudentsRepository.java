package uk.co.codesatori.backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uk.co.codesatori.backend.model.ClassOfStudents;

public interface ClassOfStudentsRepository extends CrudRepository<ClassOfStudents, UUID> {
  List<ClassOfStudents> findByTeacherId(UUID teacherId);
}
