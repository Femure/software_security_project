package ku.api.controller;

import jakarta.validation.Valid;
import ku.api.dto.RestaurantRequest;
import ku.api.dto.RestaurantResponse;
import ku.api.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService service;

    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {
        return service.getRestaurants();
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody RestaurantRequest restaurant,
            BindingResult result) {

        if (result.hasErrors())
            return new ResponseEntity<>("Invalid request format", HttpStatus.UNPROCESSABLE_ENTITY);

        service.create(restaurant);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
