package ku.chirpchat.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ku.chirpchat.dto.CommentRequest;
import ku.chirpchat.dto.CommentResponse;
import ku.chirpchat.dto.RestaurantRequest;
import ku.chirpchat.dto.RestaurantResponse;
import ku.chirpchat.service.CommentService;
import ku.chirpchat.service.RestaurantService;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/restaurant")
    public String getRestaurantPage(Model model) {
        if (!model.containsAttribute("restaurantRequest")) {
            model.addAttribute("restaurantRequest", new RestaurantRequest());
        }
        List<RestaurantResponse> listRestaurant = restaurantService.getRestaurants();
        model.addAttribute("restaurants", listRestaurant);
        listRestaurant.forEach(
                restaurant -> {
                    List<CommentResponse> listComments = commentService.getRestaurantComments(restaurant.getId());
                    restaurant.setComments(listComments);
                });
        return "restaurant";
    }

    @PostMapping("/restaurant/add")
    public String addRestaurant(@Valid RestaurantRequest restaurant,
            BindingResult result, RedirectAttributes attr, HttpSession session) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.restaurantRequest", result);
            attr.addFlashAttribute("restaurantRequest", restaurant);
            return "redirect:/restaurant";
        }

        restaurantService.create(restaurant);
        return "redirect:/restaurant";
    }

    @PostMapping("/comment/add/{restaurantId}")
    public String createComment(@PathVariable UUID restaurantId, @ModelAttribute CommentRequest comment,
            Principal principal, Model model) {
        String username = principal.getName();
        comment.setUsername(username);
        comment.setRestaurantId(restaurantId);
        commentService.createComment(comment);
        return "redirect:/restaurant";
    }

    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/restaurant";
    }

}
