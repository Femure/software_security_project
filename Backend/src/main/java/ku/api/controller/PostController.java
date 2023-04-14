package ku.api.controller;

import jakarta.validation.Valid;
import ku.api.dto.PostRequest;
import ku.api.dto.PostResponse;
import ku.api.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService service;

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return service.getPosts();
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody PostRequest post,
            BindingResult result) {

        if (result.hasErrors())
            return new ResponseEntity<>("Invalid request format", HttpStatus.UNPROCESSABLE_ENTITY);

        service.create(post);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }
}
