package ku.chirpchat.service;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ku.chirpchat.dto.SignupDto;
import ku.chirpchat.model.Member;
import ku.chirpchat.repository.MemberRepository;

@Service
public class AuthService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private static final long LOCK_TIME_DURATION = 15 * 60 * 1000; // 15 min

    public SignupDto getMemberUsername(String username) {
        Member member = repository.findByUsername(username);
        SignupDto user = null;
        if (member != null) {
            user = modelMapper.map(member, SignupDto.class);
        }
        return user;
    }

    public void increaseFailedAttempts(String username) {
        Member member = repository.findByUsername(username);
        member.setFailedAttempt(member.getFailedAttempt() + 1);
        repository.save(member);
    }

    public void resetFailedAttempts(String username) {
        Member member = repository.findByUsername(username);
        member.setFailedAttempt(0);
        repository.save(member);
    }

    public void lock(String username) {
        Member member = repository.findByUsername(username);
        member.setAccountNonLocked(true);
        member.setLockTime(new Date());
        repository.save(member);
        emailService.sendAccountLocked(member);
    }

    public boolean unlockWhenTimeExpired(String username) {
        Member member = repository.findByUsername(username);
        long lockTimeInMillis = member.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            member.setAccountNonLocked(false);
            member.setLockTime(null);
            member.setFailedAttempt(0);
            repository.save(member);
            return true;
        }
        return false;
    }

}
