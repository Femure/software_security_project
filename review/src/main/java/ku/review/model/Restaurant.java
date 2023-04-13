package ku.review.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Restaurant {

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(name = "restaurant_id", updatable = false, nullable = false)
        @JdbcTypeCode(java.sql.Types.NVARCHAR)
        private UUID id;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(name, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String name;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(address, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String address;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(rating, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private int rating;
        private Instant createdAt;

        @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @ToString.Exclude
        private List<Comment> comments;
}
