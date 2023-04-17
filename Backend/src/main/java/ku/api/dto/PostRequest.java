package ku.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;

@Data
public class PostRequest {

   private String username;


   @NotBlank(message = "Can't be blank")
   @Length(max = 250, message = "Lenght superior to 250 caracters")
   private String postText;
}
