package ku.chirpchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ku.chirpchat.dto.CommentRequest;
import ku.chirpchat.dto.CommentResponse;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CommentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtAccessTokenService tokenService;

    Logger logger = LoggerFactory.getLogger(CommentService.class);

    public List<CommentResponse> getPostComments(UUID postId) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8091/api/comment/" + postId;

        ResponseEntity<CommentResponse[]> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, CommentResponse[].class);

        CommentResponse[] comments = response.getBody();
        return Arrays.asList(comments);
    }

    public void createComment(CommentRequest comment) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<CommentRequest> entity = new HttpEntity<>(comment, headers);

        String url = "http://localhost:8091/api/comment";

        ResponseEntity<CommentRequest> response = restTemplate.exchange(url, HttpMethod.POST,
                entity, CommentRequest.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("User " + response.getBody().getUsername() + " add a comment :" +
                    "\n message : " + response.getBody().getCommentText() +
                    "\n under the post : " + response.getBody().getPostId() +
                    "\n at " + Instant.now());
        }
    }

    public void deleteComment(UUID commentId, String username) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8091/api/comment/delete/" + commentId;

        ResponseEntity<CommentRequest> response = restTemplate.exchange(url, HttpMethod.DELETE,
                entity, CommentRequest.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info(
                    "User " + username + "  remove the comment :" +
                            "\n message : " + response.getBody().getCommentText() +
                            "\n under the post : " + response.getBody().getPostId() +
                            "\n at " + Instant.now());
        }

    }
}
