package ku.chirpchat.service;

import java.time.Instant;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ku.chirpchat.dto.SignupDto;
import ku.chirpchat.model.Member;
import ku.chirpchat.repository.MemberRepository;
import ku.chirpchat.repository.TokenRepository;

import javax.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private static final long LOCK_TIME_DURATION = 15 * 60 * 1000; // 15 min

    public SignupDto getMemberUsername(String username) {
        Member member = memberRepository.findByUsername(username);
        SignupDto user = null;
        if (member != null) {
            user = modelMapper.map(member, SignupDto.class);
        }
        return user;
    }

    public void increaseFailedAttempts(String username) {
        Member member = memberRepository.findByUsername(username);
        member.setFailedAttempt(member.getFailedAttempt() + 1);
        memberRepository.save(member);
    }

    public void resetFailedAttempts(String username) {
        Member member = memberRepository.findByUsername(username);
        member.setFailedAttempt(0);
        memberRepository.save(member);
    }

    @Transactional
    public void resetEmailSentNumber(String username) {
        Member member = memberRepository.findByUsername(username);
        member.setEmailSentNumber(0);
        tokenRepository.deleteByMemberId(member.getId());
        memberRepository.save(member);
    }

    public void lock(String username) {
        Member member = memberRepository.findByUsername(username);
        member.setAccountLocked(true);
        member.setLockTime(Instant.now());
        memberRepository.save(member);
        emailService.sendAccountLocked(member);
    }

    public boolean unlockWhenTimeExpired(String username) {
        Member member = memberRepository.findByUsername(username);
        long lockTimeInMillis = member.getLockTime().toEpochMilli();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            member.setAccountLocked(false);
            member.setLockTime(null);
            member.setFailedAttempt(0);
            memberRepository.save(member);
            return true;
        }
        return false;
    }
}