package ku.project.service;

import ku.project.repository.MemberRepository;
import ku.project.dto.SignupDto;
import ku.project.model.Member;
import net.bytebuddy.utility.RandomString;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SignupService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    Logger logger = LoggerFactory.getLogger(SignupService.class);

    private static final long EXPIRATION_TIME_DURATION = 10 * 60 * 1000; // 10 min
    private static final long COOLDOWN_RESEND_TIME_DURATION = 2 * 60 * 1000; // 2min

    public boolean isUsernameAvailable(String username) {
        return repository.findByUsername(username) == null;
    }

    public boolean isEmailAvailable(String email) {
        return repository.findByEmail(email) == null;
    }

    public Member getMember(String username) {
        return repository.findByUsername(username);
    }

    public String createMember(SignupDto user) {
        Member newMember = modelMapper.map(user, Member.class);
        newMember.setCreatedAt(Instant.now());

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        newMember.setPassword(hashedPassword);
        newMember.setRole("ROLE_USER");
        newMember = setValidationEmailAttributes(newMember);
        logger.info(user.getUsername() + " has successfully logged in at " + Instant.now());

        return newMember.getVerificationCode();
    }

    public String resendVerificationEmail(String code) {
        Member member = repository.findByVerificationCode(code);
        if (member != null && !member.isEnabled()) {
            if (member.getEmailResentCooldown() == null) {
                logger.info("Resend email for user : " + member.getUsername() + " at " + Instant.now());
                member = setValidationEmailAttributes(member);
                return member.getVerificationCode();
            } else {
                long resendCoolDownTimeInMillis = member.getEmailResentCooldown().getTime();
                long currentTimeInMillis = System.currentTimeMillis();
                if (resendCoolDownTimeInMillis + COOLDOWN_RESEND_TIME_DURATION < currentTimeInMillis) {
                    logger.info("Resend email for user : " + member.getUsername() + " at " + Instant.now());
                    setValidationEmailAttributes(member);
                    return member.getVerificationCode();
                }
            }
        }
        return code;
    }

    private Member setValidationEmailAttributes(Member member) {
        String randomCode = RandomString.make(64);
        member.setVerificationCode(randomCode);
        long currentTime = System.currentTimeMillis();
        member.setExpirationTime(currentTime + EXPIRATION_TIME_DURATION);
        member.setEmailResentCooldown(new Date());
        member.setEnabled(false);

        emailService.sendVerificationEmail(member);
        repository.save(member);
        return member;
    }

    public boolean verify(String verificationCode) {
        Member member = repository.findByVerificationCode(verificationCode);

        if (member != null) {
            long expirationTime = member.getExpirationTime();
            long currentTime = System.currentTimeMillis();
            member.setVerificationCode(null);
            // if the message has been validated before 5min
            if (expirationTime > currentTime) {
                logger.info("Success verify user : " + member.getUsername() + " at " + Instant.now());
                member.setEnabled(true);
                repository.save(member);
                return true;
            }
            repository.save(member);
        }
        logger.info("Fail verify at " + Instant.now());
        return false;
    }
}
