package ku.project.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.UUID;


@Data
@NoArgsConstructor
@Entity
public class Restaurant {

   @Id
   @GeneratedValue(generator = "UUID")
   @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
   @Column(name = "id", updatable = false, nullable = false)
   @Type(type = "org.hibernate.type.UUIDCharType")
   private UUID id;

   
   private String name;
   private String address;
   private int rating;
   private Instant createdAt;
}
