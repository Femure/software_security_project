package ku.chirpchat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import kotlin.collections.builders.ListBuilder;
import ku.chirpchat.dto.CommentRequest;
import ku.chirpchat.dto.CommentResponse;
import ku.chirpchat.dto.PostResponse;
import ku.chirpchat.dto.SignupDto;
import ku.chirpchat.service.CommentService;
import ku.chirpchat.service.PostService;
import ku.chirpchat.service.MemberService;
import ku.chirpchat.service.TokenService;

import org.springframework.validation.BindingResult;

import javax.validation.Valid;

import java.security.Principal;

import javax.servlet.http.HttpSession;

@Controller
public class UserPageController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/user-page")
    public String getPostPage(Principal principal, Model model) {
        String username = principal.getName();
        SignupDto member = memberService.getMemberUsername(username);
        model.addAttribute("member", member);
        if (principal.toString().contains("google")) {
            model.addAttribute("googleUser", true);
        } else {
            model.addAttribute("googleUser", false);
        }

        if (!model.containsAttribute("commentRequest")) {
            model.addAttribute("commentRequest", new CommentRequest());
        }

        List<PostResponse> listPostUser = new ListBuilder<>();
        List<PostResponse> listPost = postService.getPosts();
        listPost.forEach(
                post -> {
                    // all posts created by the user
                    List<CommentResponse> listComments = commentService.getPostComments(post.getId());
                    if (post.getUsername().matches(username)) {
                        listPostUser.add(post);
                    }
                    // all posts where the user commented
                    else {
                        listComments.forEach(
                                comment -> {
                                    if (comment.getUsername().matches(username)) {
                                        listPostUser.add(post);
                                    }
                                });
                    }
                    post.setComments(listComments);
                });
        model.addAttribute("posts", listPostUser);

        return "user-page";
    }

    @GetMapping("/reset-password")
    public String viewResetPassword(Principal principal, SignupDto user, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null && !memberService.isMemberRegistered(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else{
            return "settings/reset-password";
        }  
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid SignupDto user, BindingResult result,
            HttpSession session, Principal principal, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null && !memberService.isMemberRegistered(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }  else {
            if (result.hasFieldErrors("password") || result.hasFieldErrors("confirmPassword")) {
                return "settings/reset-password";
            }
            int resp = 0;
            if (principal != null) {
                resp = memberService.resetPassword(user.getPassword(), principal.getName());
                if (resp == 1) {
                    model.addAttribute("valid",
                            "Your password has been successfully changed !");
                }
            } else {
                resp = tokenService.resetPassword(user.getPassword(), token);
                if (resp == 1) {
                    session.removeAttribute("token");
                    model.addAttribute("valid",
                            "Your password has been successfully changed. Go to login page to connect you.");
                }
            }
            if (resp == 0) {
                model.addAttribute("error", "Select a password different from the previous!");
            }
            return "settings/reset-password";
        }
    }

    @GetMapping("/delete-account")
    public String viewDeleteAccount(Principal principal, SignupDto user) {
        if (!memberService.isMemberRegistered(principal.getName()) || principal.getName().contains("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        else{
             return "settings/delete-account";
        }
    }

    @PostMapping("/delete-account")
    public String resetPassword(@RequestParam("password") String password, Principal principal, Model model) {
        if (!memberService.isMemberRegistered(principal.getName()) || principal.getName().contains("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            int resp = memberService.deleteAccount(password, principal.getName());
            if (resp == 1) {
                return "redirect:/logout";
            } else {
                model.addAttribute("error", "Invalid password !");
            }
            return "settings/delete-account";
        }
    }

}
