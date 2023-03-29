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

    private static final long VALIDATION_TIME_DURATION = 10 * 60 * 1000; // 10 min

    public boolean isUsernameAvailable(String username) {
        return repository.findByUsername(username) == null;
    }

    public boolean isEmailAvailable(String email) {
        return repository.findByEmail(email) == null;
    }

    public Member getMember(String username) {
        return repository.findByUsername(username);
    }

    public String createMember(SignupDto member) throws UnsupportedEncodingException, MessagingException {
        Member newMember = modelMapper.map(member, Member.class);
        newMember.setCreatedAt(Instant.now());

        String hashedPassword = passwordEncoder.encode(member.getPassword());

        newMember.setPassword(hashedPassword);

        String randomCode = RandomString.make(64);

        newMember.setVerificationCode(randomCode);
        newMember.setEnabled(false);
        newMember.setValidationTime(new Date());

        newMember.setRole("ROLE_USER");

        emailService.sendVerificationEmail(newMember);
        repository.save(newMember);

        return randomCode;
    }

    public String resendVerificationEmail(String code)
            throws MessagingException, UnsupportedEncodingException {
        Member member = repository.findByVerificationCode(code);
        if (member == null) {
            return null;
        } else {
            String randomCode = RandomString.make(64);

            member.setVerificationCode(randomCode);
            member.setEnabled(false);
            member.setValidationTime(new Date());

            emailService.sendVerificationEmail(member);
            repository.save(member);
            return randomCode;
        }

    }

    public boolean verify(String verificationCode) {
        Member member = repository.findByVerificationCode(verificationCode);

        if (member == null) {
            return false;
        } else {
            long validationTimeInMillis = member.getValidationTime().getTime();
            long currentTimeInMillis = System.currentTimeMillis();
            member.setVerificationCode(null);

            if (validationTimeInMillis + VALIDATION_TIME_DURATION < currentTimeInMillis) {
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
