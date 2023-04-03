package ku.review.controller;

import jakarta.validation.Valid;
import ku.review.dto.CommentRequest;
import ku.review.dto.CommentResponse;
import ku.review.model.Comment;
import ku.review.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService service;

    @GetMapping
    public List<Comment> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public List<CommentResponse> getAllCommentsForRestaurant(@PathVariable UUID id) {
        return service.getAllCommentsForRestaurant(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CommentRequest comment,
            BindingResult result) {

        if (result.hasErrors())
            return new ResponseEntity<String>("Invalid request format", HttpStatus.UNPROCESSABLE_ENTITY);

        service.addReview(comment);
        return new ResponseEntity<CommentRequest>(comment, HttpStatus.OK);
    }
}
