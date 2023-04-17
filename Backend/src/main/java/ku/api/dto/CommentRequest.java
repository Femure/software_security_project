package ku.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import lombok.Data;
import java.util.UUID;

@Data
public class CommentRequest {

   private UUID postId;

   @NotBlank
   private String username;

   @NotBlank
   @Length(max = 300, message = "Length superior to 300 caracters !")
   private String commentText;
}
