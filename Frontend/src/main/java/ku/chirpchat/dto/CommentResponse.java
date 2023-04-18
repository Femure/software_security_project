package ku.chirpchat.dto;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class CommentResponse {
   private UUID id;
   private Date createdAt;
   private String username;
   private String commentText;
}
