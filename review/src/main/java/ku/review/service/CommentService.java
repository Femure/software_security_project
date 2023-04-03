package ku.review.service;

import ku.review.repository.CommentRepository;
import ku.review.dto.CommentRequest;
import ku.review.dto.CommentResponse;
import ku.review.model.Comment;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Comment> getAll() {
        return repository.findAll();
    }

    public List<CommentResponse> getAllCommentsForRestaurant(UUID restaurantId) {
        List<Comment> comments = repository.findByRestaurantId(restaurantId);

        List<CommentResponse> dtos = comments
                .stream()
                .map(comment -> modelMapper.map(comment, CommentResponse.class))
                .collect(Collectors.toList());

        return dtos;
    }

    public void addReview(CommentRequest commentRequest) {
        Comment comment = modelMapper.map(commentRequest, Comment.class);
        comment.setCreatedAt(Instant.now());
        repository.save(comment);
    }
}
