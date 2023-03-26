package ku.review.controller;

import jakarta.validation.Valid;
import ku.review.dto.RestaurantRequest;
import ku.review.dto.RestaurantResponse;
import ku.review.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService service;

    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return service.getRestaurants();
    }

    @GetMapping("/{id}")
    public RestaurantResponse getRestaurantById(@PathVariable UUID id) {
        return service.getRestaurantById(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody RestaurantRequest restaurant,
            BindingResult result) {

        if (result.hasErrors())
            return new ResponseEntity<String>("Invalid request format", HttpStatus.UNPROCESSABLE_ENTITY);

        service.create(restaurant);
        return new ResponseEntity<RestaurantRequest>(restaurant, HttpStatus.OK);
    }
}
