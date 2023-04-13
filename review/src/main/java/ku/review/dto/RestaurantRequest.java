package ku.review.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class RestaurantRequest {

   @NotBlank(message = "Name is required")
   private String name;

   @NotBlank(message = "Address is required")
   private String address;

   @NotNull(message = "Rate is required")
   @Range(min = 0, max = 5, message = "Rate must be between 0 and 5")
   private Integer rating;
}
