package ku.project.service;

import java.time.Instant;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ku.project.repository.MemberRepository;

//Remove all user not fully registrated with email validation after 1 hour
@Service
@Transactional
public class RemoveUnvalidatedUserService {

    Logger logger = LoggerFactory.getLogger(RemoveUnvalidatedUserService.class);

    @Autowired
    private MemberRepository repository;
    
    @Scheduled(cron = "@hourly") //fixedDelay = 1000
    public void removeUnvalidatedUser() {
        logger.info("Remove all unvalidated user at " + Instant.now());
        repository.deleteAllUnvalidatedUser();
    }
    
}
