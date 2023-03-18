package ku.project.service;

import ku.project.dto.SignupDto;
import ku.project.model.Member;
import ku.project.repository.MemberRepository;
import net.bytebuddy.utility.RandomString;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SignupService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender mailSender;

    public boolean isUsernameAvailable(String username) {
        return repository.findByUsername(username) == null;
    }

    public boolean isEmailAvailable(String email) {
        return repository.findByEmail(email) == null;
    }

    public int createMember(SignupDto member, String siteURL) throws UnsupportedEncodingException, MessagingException {
        Member newMember = modelMapper.map(member, Member.class);
        newMember.setCreatedAt(Instant.now());

        String hashedPassword = passwordEncoder.encode(member.getPassword());

        newMember.setPassword(hashedPassword);

        String randomCode = RandomString.make(64);

        newMember.setVerificationCode(randomCode);
        newMember.setEnabled(false);

        sendVerificationEmail(newMember, siteURL);
        repository.save(newMember);

        return 1;
    }

    public Member getMember(String username) {
        return repository.findByUsername(username);
    }

    private void sendVerificationEmail(Member member, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = member.getEmail();
        String fromAddress = "maxime.f@ku.th";
        String senderName = "NFTop Compagny";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "NFTop Compagny";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", member.getUsername());
        String verifyURL = siteURL + "/verify?code=" + member.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    // public void resendVerificationEmail(String mail, String siteURL) throws MessagingException, UnsupportedEncodingException  {
    //     Member member = repository.findByEmail(mail);
    //     String randomCode = RandomString.make(64);

    //     member.setVerificationCode(randomCode);
    //     member.setEnabled(false);

    //     sendVerificationEmail(member, siteURL);
    //     repository.save(member);
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
