package ku.chirpchat.service;

import java.time.Instant;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ku.chirpchat.model.Member;
import ku.chirpchat.repository.TokenRepository;
import ku.chirpchat.repository.MemberRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Remove all user not fully registrated with email validation after 1 hour
@Service
@Transactional
public class CleanDatabaseService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    Logger logger = LoggerFactory.getLogger(CleanDatabaseService.class);

    private static final long TIME_DURATION_BEFORE_EXPIRATION = 30 * 60 * 1000; //Time before before the user registration expired if he didn't completly fulfilled it.

    @Scheduled(fixedDelay = 10 * 60 * 1000) // Check all the 10 min
    public void removeExpiredTokent() {
        long currentTimeInMillis = System.currentTimeMillis();
        tokenRepository.deleteAllExpiredToken(currentTimeInMillis);
        logger.info("Remove all expired tokens at " + Instant.now());
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000) // Check all the 10 min
    public void removeExpiredUser() {
        long currentTimeInMillis = System.currentTimeMillis();
        List<Member> listMember = memberRepository.findAllEnabledMember();
        listMember.forEach(member -> {
            long resendCoolDownTimeInMillis = member.getCreatedAt().toEpochMilli();
            if (resendCoolDownTimeInMillis + TIME_DURATION_BEFORE_EXPIRATION < currentTimeInMillis) {
                memberRepository.delete(member);
            }
        });
        logger.info("Remove all expired users");
    }
}
