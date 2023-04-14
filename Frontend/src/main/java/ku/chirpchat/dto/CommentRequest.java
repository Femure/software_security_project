package ku.chirpchat.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CommentRequest {
   private UUID postId;
   private String username;
   private String commentText;
}
