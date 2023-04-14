package ku.review.service;

import ku.review.repository.RestaurantRepository;
import ku.review.dto.RestaurantRequest;
import ku.review.dto.RestaurantResponse;
import ku.review.model.Restaurant;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    // ----> we are mapping DAO → DTO
    public List<RestaurantResponse> getRestaurants() {
        List<Restaurant> restaurants = repository.findAll();

        List<RestaurantResponse> dtos = restaurants
                .stream()
                .map(restaurant -> modelMapper.map(restaurant,
                        RestaurantResponse.class))
                .toList();

        return dtos;
    }

    // ----> we are mapping DTO → DAO
    public void create(RestaurantRequest restaurantDto) {
        Restaurant restaurant = modelMapper.map(restaurantDto,
                Restaurant.class);
        restaurant.setCreatedAt(Instant.now());
        repository.save(restaurant);
    }

}
