package api.mcnc.surveyresponseservice.entity.delete;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * please explain class!
 *
 * @author :Uheejoon
 * @since :2024-12-19 오후 8:12
 */
@Entity
@Table(name = "DELETE_QUEUE")
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteQueueEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Getter
  @Column(name = "SURVEY_ID")
  private String surveyId;
  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;

  public static DeleteQueueEntity of(String surveyId) {
    return DeleteQueueEntity.builder()
      .surveyId(surveyId)
      .build();
  }
}
