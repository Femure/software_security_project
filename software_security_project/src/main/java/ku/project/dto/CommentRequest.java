package ku.project.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CommentRequest {
   private UUID restaurantId;
   private String username;
   private String commentText;
}
