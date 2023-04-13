package ku.project.dto;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class CommentResponse {
   private UUID id;
   private String username;
   private String commentText;
   private Instant createdAt;
}
