package ku.project.repository;

import ku.project.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;


public interface MemberRepository extends JpaRepository<Member, UUID> {

   Member findByUsername(String username);

   Member findByEmail(String email);

   Member findByVerificationTokenVerificationCode(String code);

   @Modifying
   @Query("DELETE FROM Member as m WHERE m.enabled = FALSE")
   void  deleteAllUnvalidatedUser(long now);
}
