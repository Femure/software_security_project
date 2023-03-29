package ku.project.repository;

import ku.project.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.UUID;


public interface MemberRepository extends JpaRepository<Member, UUID> {

   Member findByUsername(String username);

   Member findByEmail(String email);

   Member findByVerificationCode(String code);

   @Modifying
   @Query("delete from Member m where m.createdAt <= ?1 and m.enabled = FALSE")
   void  deleteAllUnvalidatedUser(Instant now);
}
