package ku.project.controller;

import ku.project.dto.CommentRequest;
import ku.project.service.RestaurantService;
import ku.project.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/comment")
public class CommentController {

   @Autowired
   private RestaurantService restaurantService;

   @Autowired
   private CommentService commentService;

   @PostMapping("/add")
   public String createcomment(@ModelAttribute CommentRequest comment,
                              Model model, Principal principal) {
       String username = principal.getName();
       comment.setUsername(username);
       commentService.createComment(comment);
       return "redirect:/comment/" + comment.getRestaurantId();
   }

}
