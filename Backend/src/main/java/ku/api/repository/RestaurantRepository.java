package ku.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ku.api.model.Restaurant;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
}