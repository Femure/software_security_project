package ku.project.service;

import ku.project.model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(Member member, String siteURL)
            throws MessagingException, UnsupportedEncodingException {

        String subject = "Please verify your registration";
        String content = "Dear [[username]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Kin Kao Compagny";

        content = content.replace("[[username]]", member.getUsername());
        String verifyURL = siteURL + "/verify?code=" + member.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        sendEmail(member.getEmail(), subject, content);

    }

    public void sendAccountLocked(Member member)
            throws MessagingException, UnsupportedEncodingException {

        String subject = "Security alerte";
        String content = "We noticed an attempt to login to your [[username]] account which seems suspicious.<br>"
                + "We have suspended your account during 15 minutes after these delay, you will be able to retry to connect to your account.<br>"
                +"If it wasn't you:<br>"
                +"<ul><li> We advise you to log in to your account by going to our site, after the 15 minutes delay."
                +"Once logged in, go to your account, then to your settings and change your password.<br> </li></ul>"
                + "Thank you,<br>"
                + "Kin Kao Compagny";

        content = content.replace("[[username]]", member.getUsername());
        sendEmail(member.getEmail(), subject, content);

    }

    private void sendEmail(String email, String subject, String content)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = email;
        String fromAddress = "maxime.f@ku.th";
        String senderName = "Kin Kao Compagny";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
