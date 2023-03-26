package ku.project.repository;

import ku.project.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface MemberRepository extends JpaRepository<Member, UUID> {

   Member findByUsername(String username);

   Member findByEmail(String email);

   Member findByVerificationCode(String code);
}
