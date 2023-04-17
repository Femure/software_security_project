package ku.chirpchat.dto;

import lombok.Data;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Data
public class PostRequest {

   private String username;

   @NotBlank(message = "Can't be blank")
   @Length(max = 250, message = "Length superior to 250 caracters !")
   private String postText;
   
}
