package uk.co.codesatori.backend.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class ClassOfStudentsController {

  @Autowired
  SecurityService securityService;

  private final ClassOfStudentsRepository classOfStudentsRepository;

  @Autowired
  public ClassOfStudentsController(ClassOfStudentsRepository classOfStudentsRepository) {
    this.classOfStudentsRepository = classOfStudentsRepository;
  }

  @GetMapping("/classes/teacher/{id}")
  public List<ClassOfStudents> getClassOfStudentsByTeacher(@PathVariable String id) {
    /* TODO: I think this seems right...? Please double check. */
    if (!id.equals(securityService.getUser().getUid())) {
      return null;
    }
    UUID teacherId = User.FirebaseUUID.toUUID(id);
    return StreamSupport.stream(classOfStudentsRepository.findAll().spliterator(), false)
        .filter(classOfStudents -> classOfStudents.getTeacherId().equals(teacherId))
        .collect(Collectors.toList());
  }
}
