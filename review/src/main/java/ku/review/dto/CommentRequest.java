package ku.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;

@Data
public class CommentRequest {

   private UUID restaurantId;

   @NotBlank
   private String username;

   @NotBlank
   private String commentText;
}
