package ku.project.service;

import ku.project.model.Token;
import ku.project.model.VerificationToken;
import ku.project.repository.MemberRepository;
import net.bytebuddy.utility.RandomString;

import java.util.Date;

import ku.project.model.Member;
import ku.project.model.PasswordResetToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TokenService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    Logger logger = LoggerFactory.getLogger(TokenService.class);

    private static final long COOLDOWN_RESEND_TIME_DURATION = 2 * 60 * 1000; // 2min

    private static final long EXPIRATION_TIME_DURATION = 10 * 60 * 1000; // 10 min

    public Member setValidationEmailAttributes(Member member, int choice) {
        String randomCode = RandomString.make(64);
        Token token;
        if (choice == 0) {
            token = new VerificationToken();
        } else {
            token = new PasswordResetToken();
        }
        token.setVerificationCode(randomCode);
        long currentTime = System.currentTimeMillis();
        token.setExpirationTime(currentTime + EXPIRATION_TIME_DURATION);
        token.setEmailResentCooldown(new Date());
        member.setEnabled(false);
        member.setToken(token);
        if (choice == 0) {
            emailService.sendVerificationEmail(member);
        } else {
            emailService.sendResetPasswordEmail(member);
        }
        repository.save(member);
        return member;
    }

    public String resendTokenEmail(String code) {
        Member member = repository.findByTokenVerificationCode(code);
        if (member != null && !member.isEnabled()) {
            Token verificationToken = member.getToken();
            if (verificationToken.getEmailResentCooldown() == null) {
                logger.info("Resend email for user : " + member.getUsername() + " at " + Instant.now());
                member = this.setValidationEmailAttributes(member, 0);
                return member.getToken().getVerificationCode();
            } else {
                long resendCoolDownTimeInMillis = verificationToken.getEmailResentCooldown().getTime();
                long currentTimeInMillis = System.currentTimeMillis();
                if (resendCoolDownTimeInMillis + COOLDOWN_RESEND_TIME_DURATION < currentTimeInMillis) {
                    logger.info("Resend email for user : " + member.getUsername() + " at " + Instant.now());
                    member = this.setValidationEmailAttributes(member, 0);
                    return verificationToken.getVerificationCode();
                }
            }
        }
        return code;
    }

    public boolean forgotPassword(String email) {
        Member member = repository.findByEmail(email);
        if (member != null) {
            this.setValidationEmailAttributes(member, 1);
            return true;
        }
        return false;
    }

    public int resetPassword(String password, String token) {
        Member member = repository.findByTokenVerificationCode(token);
        if (member != null) {
            if (!passwordEncoder.matches(password, member.getPassword())) {
                String hashedPassword = passwordEncoder.encode(password);
                member.setPassword(hashedPassword);
                Token passwordResetToken = member.getToken();
                passwordResetToken.setVerificationCode(null);
                member.setToken(passwordResetToken);
                repository.save(member);
                logger.info("Success reset password user : " + member.getUsername() + " at " + Instant.now());
                return 2;
            }
            return 1;
        }
        return 0;
    }

    public int verifyToken(String verificationCode) {
        Member member = repository.findByTokenVerificationCode(verificationCode);

        if (member != null) {
            Token verificationToken = member.getToken();
            long expirationTime = verificationToken.getExpirationTime();
            long currentTime = System.currentTimeMillis();
            // if the message has been validated before 5min
            if (expirationTime > currentTime) {
                logger.info("Success verify user : " + member.getUsername() + " at " + Instant.now());
                if (verificationToken.getClass() == VerificationToken.class) {
                    logger.info("Successful registration");
                    member.setEnabled(true);
                    verificationToken.setVerificationCode(null);
                    member.setToken(verificationToken);
                    repository.save(member);
                    return 1;
                } else {
                    // resetPasswordVerification
                    logger.info("Redirect reset password");
                    return 2;
                }
            }
            repository.save(member);
        }
        logger.info("Fail verify at " + Instant.now());
        return 0;
    }
}
