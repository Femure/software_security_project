package ku.review.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

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
   @JdbcTypeCode(java.sql.Types.NVARCHAR)
   private UUID id;

   private String name;
   private String address;
   private int rating;
   private Instant createdAt;
}
