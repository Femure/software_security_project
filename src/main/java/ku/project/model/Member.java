package ku.project.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Member {

   @Id
   @GeneratedValue
   private UUID id;

   private Instant createdAt;
   private String firstName;
   private String lastName;
   private String username;
   private String email;
   private String password;
   private String confirmPassword;

   private String verificationCode;
   private boolean enabled;
}
