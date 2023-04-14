package ku.chirpchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ku.chirpchat.model.Member;

import java.util.List;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

   Member findByUsername(String username);

   Member findByEmail(String email);

   Member findByTokenVerificationCode(String code);

   @Modifying
   @Query("DELETE FROM Member as m WHERE m.enabled = FALSE")
   void deleteAllUnvalidatedUser();

   @Query("SELECT m FROM Member as m WHERE m.token.verificationCode = null OR m.token.expirationTime < ?1")
   List<Member> findAllExpiredToken(long currentTimeInMillis);
}
