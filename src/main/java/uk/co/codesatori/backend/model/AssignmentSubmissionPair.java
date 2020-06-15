package uk.co.codesatori.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentSubmissionPair {
  private Assignment assignment;
  private StudentSubmission studentSubmission;

  public AssignmentSubmissionPair(Assignment assignment, StudentSubmission studentSubmission) {
    this.assignment = assignment;
    this.studentSubmission = studentSubmission;
  }

  public AssignmentSubmissionPair() {}
}