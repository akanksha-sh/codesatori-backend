package uk.co.codesatori.backend.model;

import java.util.*;
import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import uk.co.codesatori.backend.utils.JsonToMapConverter;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Assignment {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(updatable = false, nullable = false)
  private UUID assignmentId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private UUID teacherId;
  @Type(type = "jsonb")
  @Column(nullable = false, columnDefinition = "jsonb")
  @Convert(attributeName = "data", converter = JsonToMapConverter.class)
  private Map<String, Object> assignmentTemplate = new HashMap<>();
  @OneToMany
  @JoinColumn(name = "assignment_id")
  private Set<AssignmentStatus> assignmentStatus = new HashSet<>();

  public Assignment(String name, UUID teacherId,
      Map<String, Object> assignmentTemplate, Set<AssignmentStatus> assignmentStatus) {
    this.name = name;
    this.teacherId = teacherId;
    this.assignmentTemplate = assignmentTemplate;
    this.assignmentStatus = assignmentStatus;
  }

  public Assignment() {
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof Assignment) {
      Assignment that = (Assignment) obj;
      return Objects.equals(this.assignmentId, that.assignmentId) &&
          Objects.equals(this.name, that.name) &&
          Objects.equals(this.teacherId, that.teacherId) &&
          Objects.equals(this.assignmentTemplate, that.assignmentTemplate);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(assignmentId, name, teacherId, assignmentTemplate);
  }
}
