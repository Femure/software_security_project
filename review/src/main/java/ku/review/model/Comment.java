package ku.review.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class Comment {

   @Id
   @GeneratedValue
   private UUID id;

   private UUID restaurantId;
   private String username;
   private String commentText;
   private Instant createdAt;
}

