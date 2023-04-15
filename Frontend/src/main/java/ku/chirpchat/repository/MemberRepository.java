package ku.chirpchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ku.chirpchat.model.Member;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

   Member findByUsername(String username);

   Member findByEmail(String email);

   Member findByTokenVerificationCode(String code);
}
