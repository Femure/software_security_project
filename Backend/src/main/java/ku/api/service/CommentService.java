package ku.api.service;

import ku.api.repository.CommentRepository;
import ku.api.repository.RestaurantRepository;
import ku.api.dto.CommentRequest;
import ku.api.dto.CommentResponse;
import ku.api.model.Comment;
import ku.api.model.Restaurant;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<CommentResponse> getAllCommentsForRestaurant(UUID restaurantId) {
        List<Comment> comments = commentRepository.findByRestaurantId(restaurantId);
        List<CommentResponse> dtos = comments
                .stream()
                .map(comment -> modelMapper.map(comment, CommentResponse.class))
                .toList();
        return dtos;
    }

    public void addComment(CommentRequest commentRequest) {
        Restaurant restaurant = restaurantRepository.getReferenceById(commentRequest.getRestaurantId());
        Comment comment = modelMapper.map(commentRequest, Comment.class);
        comment.setCreatedAt(Instant.now());
        comment.setRestaurant(restaurant);
        commentRepository.save(comment);
    }

    public void deleteComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }
}
