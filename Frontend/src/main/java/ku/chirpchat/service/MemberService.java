package ku.chirpchat.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import ku.chirpchat.dto.SignupDto;
import ku.chirpchat.model.Member;
import ku.chirpchat.repository.MemberRepository;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MemberService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(MemberService.class);

    public boolean isMemberRegistered(String username) {
        return repository.findByUsername(username) != null;
    }

    public SignupDto getMemberUsername(String username) {
        Member member = repository.findByUsername(username);
        SignupDto user = null;
        if (member != null) {
            user = modelMapper.map(member, SignupDto.class);
        }
        return user;
    }
    
    public int resetPassword(String password, String username) {
        // Reset password change password from user page
        Member member = repository.findByUsername(username);
        if (member != null) {
            if (!passwordEncoder.matches(password, member.getPassword())) {
                String hashedPassword = passwordEncoder.encode(password);
                member.setPassword(hashedPassword);
                repository.save(member);
                logger.info("Success reset password user : " + member.getUsername() + " at " + Instant.now());
                return 1;
            }
        }
        return 0;
    }

    
    public int deleteAccount(String password, String username){
        Member member = repository.findByUsername(username);
        if (member != null) {
            if (passwordEncoder.matches(password, member.getPassword())) {
                repository.delete(member);
                logger.info("Success delete account of user : " + member.getUsername() + " at " + Instant.now());
                return 1;
            }
        }
        return 0;
    }

}
