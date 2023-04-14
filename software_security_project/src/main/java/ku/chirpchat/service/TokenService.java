package ku.chirpchat.service;

import net.bytebuddy.utility.RandomString;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ku.chirpchat.model.Member;
import ku.chirpchat.model.PasswordResetToken;
import ku.chirpchat.model.Token;
import ku.chirpchat.model.VerificationToken;
import ku.chirpchat.repository.MemberRepository;

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

    private static final long EXPIRATION_TIME_DURATION = 5 * 60 * 1000; // 5 min

    public static final int MAX_SENT_EMAIL = 3;

    public Member setTokenEmailAttributes(Member member, int choice) {
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
        member.setEmailSentNumber(member.getEmailSentNumber() + 1);
        token.setMember(member);
        member.setToken(token);
        if (choice == 0) {
            emailService.sendVerificationEmail(member);
        } else {
            emailService.sendResetPasswordEmail(member);
        }
        repository.save(member);
        return member;
    }

    public String resendTokenEmail(String token, int choice) {
        Member member = repository.findByTokenVerificationCode(token);
        if (member != null) {
            Token memberToken = member.getToken();
            if (member.getEmailSentNumber() < MAX_SENT_EMAIL) {
                long resendCoolDownTimeInMillis = memberToken.getEmailResentCooldown().getTime();
                long currentTimeInMillis = System.currentTimeMillis();
                if (resendCoolDownTimeInMillis + COOLDOWN_RESEND_TIME_DURATION < currentTimeInMillis) {
                    logger.info("Resend email for user : " + member.getUsername() + " at " + Instant.now());
                    memberToken.setVerificationCode(null);
                    memberToken.setMember(null);
                    member = this.setTokenEmailAttributes(member, choice);
                    return member.getToken().getVerificationCode();
                }
            }
            else{
                return "emailSentNumberExceeded";
            }

        }
        return token;
    }

    public String forgotPassword(String email) {
        Member member = repository.findByEmail(email);
        if (member != null && member.isEnabled()) {
            this.setTokenEmailAttributes(member, 1);
            return member.getToken().getVerificationCode();
        }
        return null;
    }

    public int resetPassword(String password, String token) {
        Member member = repository.findByTokenVerificationCode(token);
        if (member != null) {
            if (!passwordEncoder.matches(password, member.getPassword())) {
                String hashedPassword = passwordEncoder.encode(password);
                member.setPassword(hashedPassword);
                Token passwordResetToken = member.getToken();
                passwordResetToken.setVerificationCode(null);
                member.setEmailSentNumber(0);
                member.setToken(passwordResetToken);
                passwordResetToken.setMember(member);
                repository.save(member);
                logger.info("Success reset password user : " + member.getUsername() + " at " + Instant.now());
                return 2;
            }
            return 1;
        }
        return 0;
    }

    public int verifyToken(String token) {
        Member member = repository.findByTokenVerificationCode(token);

        if (member != null) {
            Token memberToken = member.getToken();
            long expirationTime = memberToken.getExpirationTime();
            long currentTime = System.currentTimeMillis();
            member.setEmailSentNumber(0);
            // if the message has been validated before 5min
            if (expirationTime > currentTime) {
                logger.info("Success verify user : " + member.getUsername() + " at " + Instant.now());
                if (memberToken.getClass() == VerificationToken.class) {
                    logger.info("Successful registration");
                    member.setEnabled(true);
                    memberToken.setVerificationCode(null);
                    memberToken.setMember(null);
                    member.setToken(memberToken);
                    repository.save(member);
                    return 1;
                } else {
                    // resetPasswordVerification
                    logger.info("Redirect reset password");
                    return 2;
                }
            }
        }
        logger.info("Fail verify at " + Instant.now());
        return 0;
    }
}