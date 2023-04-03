package ku.review.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class CommentResponse {
   private String username;
   private String commentText;
   private Instant createdAt;
}
