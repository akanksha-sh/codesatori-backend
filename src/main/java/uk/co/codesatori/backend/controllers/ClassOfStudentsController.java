package uk.co.codesatori.backend.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.codesatori.backend.model.ClassOfStudents;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;

@RestController
public class ClassOfStudentsController {

  private final ClassOfStudentsRepository classOfStudentsRepository;

  @Autowired
  public ClassOfStudentsController(ClassOfStudentsRepository classOfStudentsRepository) {
    this.classOfStudentsRepository = classOfStudentsRepository;
  }

  @GetMapping("/classes/{id}")
  public ClassOfStudents getClassOfStudents(@PathVariable UUID id) {
    return classOfStudentsRepository.findById(id).orElse(null);
  }

  @GetMapping("/classes")
  public Iterable<ClassOfStudents> getAllClassesOfStudents() {
    return classOfStudentsRepository.findAll();
  }

  @PostMapping("/classes")
  void addClassOfStudents(@RequestBody ClassOfStudents classOfStudents) {
    /* TODO: shouldn't this only add if there's no entity with the id? */
    classOfStudentsRepository.save(classOfStudents);
  }

  @PutMapping("/classes")
  void updateClassOfStudents(@RequestBody ClassOfStudents classOfStudents) {
    classOfStudentsRepository.save(classOfStudents);
  }

  @DeleteMapping("/classes/{id}")
  void deleteClassOfStudents(@PathVariable UUID id) {
    classOfStudentsRepository.deleteById(id);
  }

}
