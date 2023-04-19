package ku.chirpchat.component;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ku.chirpchat.model.Member;
import ku.chirpchat.repository.MemberRepository;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (memberRepository.findByUsername("admin") == null) {
            Member admin = new Member();
            admin.setCreatedAt(Instant.now());
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setUsername("admin");
            admin.setEmail("chirpchatcompany@gmail.com");

            String hashedPassword = passwordEncoder.encode(adminPassword);
            admin.setPassword(hashedPassword);

            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);

            memberRepository.save(admin);
        }

    }

}