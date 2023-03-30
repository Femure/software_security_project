package ku.project.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class VerificationToken {

   @Id
   @GeneratedValue(generator = "UUID")
   @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
   @Column(name = "id_verification_token", updatable = false, nullable = false)
   @Type(type = "org.hibernate.type.UUIDCharType")
   private UUID id;

   private String verificationCode;
   private Date emailResentCooldown;
   private long expirationTime;

   @OneToOne(mappedBy = "verificationToken")
   private Member member;

}
