package ku.chirpchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ku.chirpchat.dto.PostRequest;
import ku.chirpchat.dto.PostResponse;

import java.util.Arrays;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtAccessTokenService tokenService;

    public PostRequest create(PostRequest post) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<PostRequest> entity = new HttpEntity<>(post, headers);

        String url = "http://localhost:8091/api/post";

        ResponseEntity<PostRequest> response = restTemplate.exchange(url, HttpMethod.POST,
                entity, PostRequest.class);

        return response.getBody();
    }

    public List<PostResponse> getPosts() {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8091/api/post";

        ResponseEntity<PostResponse[]> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, PostResponse[].class);

        PostResponse[] posts = response.getBody();
        return Arrays.asList(posts);
    }
}
