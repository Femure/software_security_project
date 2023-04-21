package ku.chirpchat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "consent")
public class Consent {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "consent_id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(columnDefinition = "VARBINARY(256)")
    @ColumnTransformer(read = "cast(AES_DECRYPT(consent_name, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
    private boolean consentName;

    @Column(columnDefinition = "VARBINARY(256)")
    @ColumnTransformer(read = "cast(AES_DECRYPT(consentip, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
    private boolean consentIP ;

    @Column(columnDefinition = "VARBINARY(256)")
    @ColumnTransformer(read = "cast(AES_DECRYPT(consent_ad, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
    private boolean consentAd;

    @Column(columnDefinition = "VARBINARY(256)")
    @ColumnTransformer(read = "cast(AES_DECRYPT(consent_news, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
    private boolean consentNews;

    @Column(columnDefinition = "VARBINARY(256)")
    @ColumnTransformer(read = "cast(AES_DECRYPT(consent_surveys, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
    private boolean consentSurveys;

    @Column(columnDefinition = "VARBINARY(256)")
    @ColumnTransformer(read = "cast(AES_DECRYPT(consent_analysis, UNHEX('F3229A0B371ED2D9441B830D21A390C3')) as char(255))", write = "AES_ENCRYPT(?, UNHEX('F3229A0B371ED2D9441B830D21A390C3'))")
    private boolean consentAnalysis;

    @OneToOne
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;

}
