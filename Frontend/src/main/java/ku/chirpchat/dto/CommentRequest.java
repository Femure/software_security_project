package ku.chirpchat.dto;

import lombok.Data;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Data
public class CommentRequest {
   private UUID postId;
   private String username;


   @NotBlank
   @Length(max = 300, message = "Length superior to 300 caracters !")
   private String commentText;
}
