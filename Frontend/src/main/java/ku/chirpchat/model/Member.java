package ku.chirpchat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
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

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(first_name, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String firstName;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(last_name, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String lastName;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(username, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String username;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(email, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String email;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(role, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private String role;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(enabled, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private boolean enabled;

        private String password;

        
        @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
        @ToString.Exclude
        Token token;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(email_sent_number, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private int emailSentNumber;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(account_non_locked, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private boolean accountNonLocked;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(failed_attempt, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private int failedAttempt;

        @Column(columnDefinition = "VARBINARY(256)")
        @ColumnTransformer(read = "cast(AES_DECRYPT(lock_time, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
        private Date lockTime;
}
