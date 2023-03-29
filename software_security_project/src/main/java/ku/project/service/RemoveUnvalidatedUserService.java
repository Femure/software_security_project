package ku.project.service;

import java.time.Instant;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ku.project.repository.MemberRepository;

//Remove all user not fully registrated with email validation after 1 hour
@Service
@Transactional
public class RemoveUnvalidatedUserService {

    @Autowired
    private MemberRepository repository;
    
    @Scheduled(cron = "@hourly")
    public void removeUnvalidatedUser() {
        System.out.println("CLEAN");
        repository.deleteAllUnvalidatedUser(Instant.now());
    }
    
}
