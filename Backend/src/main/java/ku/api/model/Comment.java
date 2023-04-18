package ku.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
public class Comment {

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        @Column(name = "id", updatable = false, nullable = false)
        @JdbcTypeCode(java.sql.Types.NVARCHAR)
        private UUID id;

        @DateTimeFormat(pattern="dd-MM-yyyy HH:mm:ss")
        private Date createdAt;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(username, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String username;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(comment_text, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String commentText;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id")
        @ToString.Exclude
        private Post post;
}
