package ku.api.service;

import ku.api.repository.PostRepository;
import ku.api.dto.PostRequest;
import ku.api.dto.PostResponse;
import ku.api.model.Post;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    // ----> we are mapping DAO → DTO
    public List<PostResponse> getPosts() {
        List<Post> posts = repository.findAll();

        List<PostResponse> dtos = posts
                .stream()
                .map(post -> modelMapper.map(post,
                        PostResponse.class))
                .toList();

        return dtos;
    }

    // ----> we are mapping DTO → DAO
    public void create(PostRequest postDto) {
        Post post = modelMapper.map(postDto,
                Post.class);
        post.setCreatedAt(Instant.now());
        repository.save(post);
    }

}
