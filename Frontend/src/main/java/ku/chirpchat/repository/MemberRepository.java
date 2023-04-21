package ku.chirpchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ku.chirpchat.model.Member;

import java.util.List;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

   Member findByUsername(String username);

   Member findByEmail(String email);

   Member findByTokenVerificationCode(String code);

   @Query("SELECT m FROM Member AS m WHERE m.enabled = FALSE")
   List<Member> findAllEnabledMember();
}
