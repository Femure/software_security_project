package ku.project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.Date;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member {

   @Id
   @GeneratedValue(generator = "UUID")
   @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
   @Column(name = "member_id", updatable = false, nullable = false)
   @Type(type = "org.hibernate.type.UUIDCharType")
   private UUID id;

   private Instant createdAt;
   private String firstName;
   private String lastName;
   private String username;
   private String email;
   private String role;
   private String password;

   private boolean enabled;

   @OneToOne(mappedBy = "member", cascade = CascadeType.ALL,
   fetch = FetchType.LAZY, optional = false)
   Token token;

   private boolean accountNonLocked;
   private int failedAttempt;
   private Date lockTime;
}
