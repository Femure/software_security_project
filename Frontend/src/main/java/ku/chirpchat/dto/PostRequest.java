package ku.chirpchat.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostRequest {

   private String username;

   @NotBlank(message = "Name is required")
   private String name;

   @NotBlank(message = "Address is required")
   private String address;

   @NotNull(message = "Rate is required")
   @Range(min = 0, max = 5, message = "Rate must be between 0 and 5")
   private Integer rating;
}
