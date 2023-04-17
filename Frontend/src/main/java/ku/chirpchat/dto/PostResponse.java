package ku.chirpchat.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class PostResponse {
   private UUID id;
   private Instant createdAt;
   private String username;
   private String postText;
   private List<CommentResponse> comments;
}
