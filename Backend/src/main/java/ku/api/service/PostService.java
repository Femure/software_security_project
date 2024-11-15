package ku.api.service;

import ku.api.repository.PostRepository;
import ku.api.dto.PostRequest;
import ku.api.dto.PostResponse;
import ku.api.model.Post;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    // ----> we are mapping DAO → DTO
    public List<PostResponse> getPosts() {
        List<Post> posts = repository.findAll(Sort.by("createdAt").descending());

        List<PostResponse> dtos = posts
                .stream()
                .map(post -> modelMapper.map(post,
                        PostResponse.class))
                .toList();

        return dtos;
    }

    // ----> we are mapping DTO → DAO
    public PostResponse createPost(PostRequest postRequest) {
        Post post = modelMapper.map(postRequest,
                Post.class);
        post.setCreatedAt(new Date());
        repository.save(post);

        PostResponse postResponse = modelMapper.map(post,
                PostResponse.class);
        return postResponse;
    }

    public PostResponse deletePost(UUID postId) {
        Optional<Post> post = repository.findById(postId);
        if (post.isPresent()) {
            PostResponse postResponse = modelMapper.map(post.get(), PostResponse.class);
            repository.deleteById(postId);
            return postResponse;
        } else {
            return null;
        }

    }

}
