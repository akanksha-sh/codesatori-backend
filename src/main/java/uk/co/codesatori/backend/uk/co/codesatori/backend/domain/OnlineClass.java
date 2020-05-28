package uk.co.codesatori.backend.uk.co.codesatori.backend.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class OnlineClass {

  public OnlineClass(String name, String code, Long teacherId) {
    this.name = name;
    this.code = code;
    this.teacherId = teacherId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long classId;

  private String name;

  private String code;

  private Long teacherId;

}
