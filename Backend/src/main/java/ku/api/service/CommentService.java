package ku.api.service;

import ku.api.repository.CommentRepository;
import ku.api.repository.PostRepository;
import ku.api.dto.CommentRequest;
import ku.api.dto.CommentResponse;
import ku.api.model.Comment;
import ku.api.model.Post;

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
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<CommentResponse> getAllCommentsForPost(UUID postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentResponse> dtos = comments
                .stream()
                .map(comment -> modelMapper.map(comment, CommentResponse.class))
                .toList();
        return dtos;
    }

    public void addComment(CommentRequest commentRequest) {
        Post post = postRepository.getReferenceById(commentRequest.getPostId());
        Comment comment = modelMapper.map(commentRequest, Comment.class);
        comment.setCreatedAt(Instant.now());
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public void deleteComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }
}
