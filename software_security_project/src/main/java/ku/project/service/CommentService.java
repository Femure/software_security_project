package ku.project.service;

import ku.project.dto.CommentRequest;
import ku.project.dto.CommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtAccessTokenService tokenService;

    public List<CommentResponse> getRestaurantComments(UUID restaurantId) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8091/api/comment/add/" + restaurantId;

        ResponseEntity<CommentResponse[]> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, CommentResponse[].class);

        CommentResponse[] comments = response.getBody();
        return Arrays.asList(comments);
    }

    public CommentRequest createComment(CommentRequest comment) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<CommentRequest> entity = new HttpEntity<>(comment, headers);

        String url = "http://localhost:8091/api/comment";

        ResponseEntity<CommentRequest> response = restTemplate.exchange(url, HttpMethod.POST,
                entity, CommentRequest.class);

        return response.getBody();
    }

    public void deleteComment(UUID commentId) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8091/api/comment/delete/" + commentId;

        restTemplate.exchange(url, HttpMethod.DELETE,
                entity, Void.class);
    }
}
