package ku.project.service;

import ku.project.dto.SignupRequest;
import ku.project.dto.SignupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SignupService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtAccessTokenService tokenService;

    public SignupRequest createMember(SignupRequest signup) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
        HttpEntity entity = new HttpEntity(signup, headers);

        String url = "http://localhost:8091/api/signup";

        ResponseEntity<SignupRequest> response = restTemplate.exchange(url, HttpMethod.POST,
                entity, SignupRequest.class);

        return response.getBody();
    }

    public SignupResponse getMember(String username) {
        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        HttpEntity entity = new HttpEntity(headers);

        String url = "http://localhost:8091/api/signup/" + username;
        ResponseEntity<SignupResponse> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, SignupResponse.class);

        return response.getBody();
    }

}
