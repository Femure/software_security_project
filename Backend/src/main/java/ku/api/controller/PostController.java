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
import java.util.UUID;

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

        return new ResponseEntity<>(service.createPost(post), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        PostResponse post  = service.deletePost(id);
        if(post == null){
            return new ResponseEntity<>("No post found for this id", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }
}
