package ku.chirpchat.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class PostResponse {
   private UUID id;
   private Date createdAt;
   private String username;
   private String postText;
   private List<CommentResponse> comments;
}
