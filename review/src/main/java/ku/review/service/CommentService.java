package ku.review.service;

import ku.review.repository.CommentRepository;
import ku.review.repository.RestaurantRepository;
import ku.review.dto.CommentRequest;
import ku.review.dto.CommentResponse;
import ku.review.model.Comment;
import ku.review.model.Restaurant;

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
                .collect(Collectors.toList());
        return dtos;
    }

    public void addComment(CommentRequest commentRequest) {
        Restaurant restaurant = restaurantRepository.getReferenceById(commentRequest.getRestaurantId());
        if (restaurant != null) {
            Comment comment = modelMapper.map(commentRequest, Comment.class);
            comment.setCreatedAt(Instant.now());
            comment.setRestaurant(restaurant);
            commentRepository.save(comment);
        }
    }

    public void deleteComment(UUID commentId){
        commentRepository.deleteById(commentId);
        System.out.println("Delete successful");
    }
}
