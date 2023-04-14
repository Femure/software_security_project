package ku.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ku.api.model.Post;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}