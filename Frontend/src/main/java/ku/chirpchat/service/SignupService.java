package ku.chirpchat.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ku.chirpchat.dto.SignupDto;
import ku.chirpchat.dto.ConsentDto;
import ku.chirpchat.model.Consent;
import ku.chirpchat.model.Member;
import ku.chirpchat.repository.MemberRepository;
import ku.chirpchat.repository.ConsentRepository;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SignupService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TokenService tokenService;

    Logger logger = LoggerFactory.getLogger(SignupService.class);

    public boolean isUsernameAvailable(String username) {
        return memberRepository.findByUsername(username) == null;
    }

    public boolean isEmailAvailable(String email) {
        return memberRepository.findByEmail(email) == null;
    }

    public Member getMember(String username) {
        return memberRepository.findByUsername(username);
    }

    public String createMember(SignupDto user) {
        Member newMember = modelMapper.map(user, Member.class);
        newMember.setCreatedAt(Instant.now());

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        newMember.setPassword(hashedPassword);
        newMember.setRole("ROLE_USER");
        newMember.setEnabled(false);
        newMember = tokenService.setTokenEmailAttributes(newMember, 0);
        logger.info(user.getUsername() + " has successfully logged in at " + Instant.now());

        return newMember.getToken().getVerificationCode();
    }

    public void createConsent(ConsentDto consentDto, String username){
        Consent consent = modelMapper.map(consentDto, Consent.class);
        Member member = this.getMember(username);
        consent.setMember(member);
        consentRepository.save(consent);
        System.out.println("Save");
    }
}
