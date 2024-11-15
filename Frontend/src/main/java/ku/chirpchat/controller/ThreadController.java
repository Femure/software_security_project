package ku.chirpchat.controller;

import java.util.List;

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
import ku.chirpchat.dto.PostRequest;
import ku.chirpchat.dto.PostResponse;
import ku.chirpchat.service.CommentService;
import ku.chirpchat.service.PostService;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Controller
public class ThreadController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/thread")
    public String viewThreadPage(Model model) {
        if (!model.containsAttribute("postRequest")) {
            model.addAttribute("postRequest", new PostRequest());
        }
        if (!model.containsAttribute("commentRequest")) {
            model.addAttribute("commentRequest", new CommentRequest());
        }
        List<PostResponse> listPost = postService.getPosts();
        model.addAttribute("posts", listPost);
        listPost.forEach(
                post -> {
                    List<CommentResponse> listComments = commentService.getPostComments(post.getId());
                    post.setComments(listComments);
                });
        return "thread";
    }

    @PostMapping("/post/add")
    public String createPost(@Valid PostRequest post,
            BindingResult result, Principal principal, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.postRequest", result);
            attr.addFlashAttribute("postRequest", post);
            return "redirect:/thread";
        }
        post.setUsername(principal.getName());
        postService.createPost(post);
        return "redirect:/thread";
    }

    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable UUID postId,  Principal principal) {
        postService.deletePost(postId,principal.getName());
        return "redirect:/thread";
    }

    @PostMapping("/comment/add/{postId}")
    public String createComment(@Valid CommentRequest comment,
            BindingResult result, @PathVariable UUID postId, Principal principal,RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.commentRequest", result);
            attr.addFlashAttribute("commentRequest", comment);
            return "redirect:/thread";
        }
        String username = principal.getName();
        comment.setUsername(username);
        comment.setPostId(postId);
        commentService.createComment(comment);
        return "redirect:/thread";
    }

    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable UUID commentId, Principal principal) {
        commentService.deleteComment(commentId,principal.getName());
        return "redirect:/thread";
    }

}
