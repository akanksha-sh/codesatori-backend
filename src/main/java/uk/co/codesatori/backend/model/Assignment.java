package uk.co.codesatori.backend.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import uk.co.codesatori.backend.utils.JsonToMapConverter;

@Entity
@Table(name = "assignments")
@Getter
@Setter
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
  @Column(nullable = false, columnDefinition = "json")
  @Convert(attributeName = "data", converter = JsonToMapConverter.class)
  private Map<String, Object> assignmentTemplate = new HashMap<>();

  public Assignment(String name, UUID teacherId,
      Map<String, Object> assignmentTemplate) {
    this.name = name;
    this.teacherId = teacherId;
    this.assignmentTemplate = assignmentTemplate;
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
