package ku.chirpchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ku.chirpchat.model.Consent;

import java.util.UUID;

public interface ConsentRepository extends JpaRepository<Consent, UUID> {

}
