package ku.project.service;

import ku.project.repository.MemberRepository;
import ku.project.dto.SignupDto;
import ku.project.model.Member;
import net.bytebuddy.utility.RandomString;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;

import javax.mail.MessagingException;

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

    public String createMember(SignupDto user) throws UnsupportedEncodingException, MessagingException {
        Member newMember = modelMapper.map(user, Member.class);
        newMember.setCreatedAt(Instant.now());

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        newMember.setPassword(hashedPassword);
        newMember.setRole("ROLE_USER");
        newMember = setValidationEmailAttributes(newMember);
        return newMember.getVerificationCode();
    }

    public String resendVerificationEmail(String code)
            throws MessagingException, UnsupportedEncodingException {
        Member member = repository.findByVerificationCode(code);
        if (member != null && !member.isEnabled()) {
            if (member.getEmailResentCooldown() == null) {
                member = setValidationEmailAttributes(member);
                return member.getVerificationCode();
            } else {
                long resendCoolDownTimeInMillis = member.getEmailResentCooldown().getTime();
                long currentTimeInMillis = System.currentTimeMillis();
                if (resendCoolDownTimeInMillis + COOLDOWN_RESEND_TIME_DURATION < currentTimeInMillis) {
                    setValidationEmailAttributes(member);
                    return member.getVerificationCode();
                }
            }
        }
        return code;
    }

    private Member setValidationEmailAttributes(Member member) throws MessagingException, UnsupportedEncodingException {
        String randomCode = RandomString.make(64);
        member.setVerificationCode(randomCode);
        member.setEnabled(false);
        member.setExpirationTime(new Date());
        member.setEmailResentCooldown(new Date());

        emailService.sendVerificationEmail(member);
        repository.save(member);
        return member;
    }

    public boolean verify(String verificationCode) {
        Member member = repository.findByVerificationCode(verificationCode);

        if (member == null) {
            return false;
        } else {
            long expirationTimeInMillis = member.getExpirationTime().getTime();
            long currentTimeInMillis = System.currentTimeMillis();
            member.setVerificationCode(null);

            if (expirationTimeInMillis + EXPIRATION_TIME_DURATION > currentTimeInMillis) {
                member.setEnabled(true);
                repository.save(member);
                return true;
            }
            // if the message hasn't been validated after 5min it's invalidate
            else {
                repository.save(member);
                return false;
            }

        }

    }

}
