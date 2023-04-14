package ku.api.repository;

import ku.api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
   
   @Query("SELECT c FROM Comment as c WHERE c.restaurant.id = ?1")
   List<Comment> findByRestaurantId(UUID restaurantId);
}
