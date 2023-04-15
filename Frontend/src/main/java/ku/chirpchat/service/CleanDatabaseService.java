package ku.chirpchat.service;

import java.time.Instant;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ku.chirpchat.repository.TokenRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Remove all user not fully registrated with email validation after 1 hour
@Service
@Transactional
public class CleanDatabaseService {

    Logger logger = LoggerFactory.getLogger(CleanDatabaseService.class);

    @Autowired
    private TokenRepository tokenRepository;

    @Scheduled(cron = "@hourly") // fixedDelay = 1000
    public void removeExpiredToken() {
        long currentTimeInMillis = System.currentTimeMillis();
        tokenRepository.deleteAllExpiredToken(currentTimeInMillis);
        logger.info("Remove all expired token at " + Instant.now());
    }

}
