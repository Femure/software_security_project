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

    public boolean isUsernameAvailable(String username) {
        return repository.findByUsername(username) == null;
    }

    public boolean isEmailAvailable(String email) {
        return repository.findByEmail(email) == null;
    }

    public Member getMember(String username) {
        return repository.findByUsername(username);
    }

    public int createMember(SignupDto member) throws UnsupportedEncodingException, MessagingException {
        Member newMember = modelMapper.map(member, Member.class);
        newMember.setCreatedAt(Instant.now());

        String hashedPassword = passwordEncoder.encode(member.getPassword());

        newMember.setPassword(hashedPassword);

        String randomCode = RandomString.make(64);

        newMember.setVerificationCode(randomCode);
        newMember.setEnabled(false);
        newMember.setRole("ROLE_USER");

        emailService.sendVerificationEmail(newMember);
        repository.save(newMember);

        return 1;
    }

    // public void resendVerificationEmail(String mail, String siteURL) throws
    // MessagingException, UnsupportedEncodingException {
    // Member member = repository.findByEmail(mail);
    // String randomCode = RandomString.make(64);

    // member.setVerificationCode(randomCode);
    // member.setEnabled(false);

    // sendVerificationEmail(member, siteURL);
    // repository.save(member);
    // }

    public boolean verify(String verificationCode) {
        Member member = repository.findByVerificationCode(verificationCode);

        if (member == null) {
            return false;
        } else {
            member.setVerificationCode(null);
            member.setEnabled(true);
            repository.save(member);

            return true;
        }

    }

}
