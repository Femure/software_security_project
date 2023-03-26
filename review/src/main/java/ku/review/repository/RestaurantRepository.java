package ku.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ku.review.model.Restaurant;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
}