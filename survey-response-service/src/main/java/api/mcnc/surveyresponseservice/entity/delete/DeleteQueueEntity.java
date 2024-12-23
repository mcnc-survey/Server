package api.mcnc.surveyresponseservice.entity.delete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
  private Long id;
  @Getter
  @Column(name = "SURVEY_ID")
  private String surveyId;
  @Column(name = "CREATED_AT")
  private Long createdAt;

  public static DeleteQueueEntity of(String surveyId) {
    return DeleteQueueEntity.builder()
      .surveyId(surveyId)
      .build();
  }
}
