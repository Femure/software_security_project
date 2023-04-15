package ku.chirpchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ku.chirpchat.model.Token;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
   
   @Modifying
   @Query("DELETE FROM Token AS t WHERE t.verificationCode = null OR (t.expirationTime < ?1)")
   void deleteAllExpiredToken(long currentTimeInMillis);
}
