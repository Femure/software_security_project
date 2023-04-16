package ku.chirpchat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kotlin.collections.builders.ListBuilder;
import ku.chirpchat.dto.CommentResponse;
import ku.chirpchat.dto.PostResponse;
import ku.chirpchat.dto.SignupDto;
import ku.chirpchat.service.CommentService;
import ku.chirpchat.service.PostService;
import ku.chirpchat.service.AuthService;

import java.security.Principal;

@Controller
public class UserPageController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService memberService;

    @GetMapping("/user-page")
    public String getPostPage(Principal principal, Model model) {
        String username = principal.getName();
        SignupDto member = memberService.getMemberUsername(username);
        model.addAttribute("member", member);

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
}
