package ku.project.service;

import ku.project.repository.MemberRepository;
import ku.project.dto.SignupDto;
import ku.project.model.Member;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private TokenService tokenService;

    Logger logger = LoggerFactory.getLogger(SignupService.class);

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
        newMember = tokenService.setValidationEmailAttributes(newMember, 0);
        logger.info(user.getUsername() + " has successfully logged in at " + Instant.now());

        return newMember.getToken().getVerificationCode();
    }

}
