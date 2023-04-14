package ku.project.service;

import ku.project.dto.RestaurantRequest;
import ku.project.dto.RestaurantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtAccessTokenService tokenService;

    public RestaurantRequest create(RestaurantRequest restaurant) {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<RestaurantRequest> entity = new HttpEntity<>(restaurant, headers);

        String url = "http://localhost:8091/api/restaurant";

        ResponseEntity<RestaurantRequest> response = restTemplate.exchange(url, HttpMethod.POST,
                entity, RestaurantRequest.class);

        return response.getBody();
    }

    public List<RestaurantResponse> getRestaurants() {

        String token = tokenService.requestAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8091/api/restaurant";

        ResponseEntity<RestaurantResponse[]> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, RestaurantResponse[].class);

        RestaurantResponse[] restaurants = response.getBody();
        return Arrays.asList(restaurants);
    }
}
