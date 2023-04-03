package ku.project.controller;

import ku.project.dto.RestaurantRequest;
import ku.project.dto.RestaurantResponse;
import ku.project.service.CommentService;
import ku.project.service.RestaurantService;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/restaurant")
    public String getRestaurantPage(Model model) {
        List<RestaurantResponse> listRestaurant = restaurantService.getRestaurants();
        model.addAttribute("restaurants", listRestaurant);
        listRestaurant.forEach(restaurant -> model.addAttribute("comments",
                commentService.getRestaurantComments(restaurant.getId())));
        return "restaurant"; // return restaurant.html
    }

    @GetMapping("/restaurant/add")
    public String getAddPage(RestaurantRequest restaurantRequest) {
        return "restaurant-add"; // return restaurant-add.html
    }

    @PostMapping("/restaurant/add")
    public String addRestaurant(@Valid RestaurantRequest restaurant,
            BindingResult result,
            Model model) {
        if (result.hasErrors())
            return "restaurant-add";

        restaurantService.create(restaurant);
        return "redirect:/restaurant";
    }

}
