package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;
    private final CommentService commentService;

    public PostPage(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @Guest
    @GetMapping("/post/{id}")
    public String post(@PathVariable String id, Model model) {
        long postId = parseLong(id);
        Post post = postId == -1 ? null : postService.findById(postId);
        model.addAttribute("vPost", post);
        model.addAttribute("newComment", new Comment());
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String post(@PathVariable String id,
                       @Valid @ModelAttribute("newComment") Comment comment,
                       HttpSession httpSession) {
        long postId = parseLong(id);
        comment.setUser(getUser(httpSession));
        Post post = postService.findById(postId);
        if (post != null) {
            comment.setPost(post);
            postService.addComment(post, comment);
            //commentService.saveComment(comment);
        }
        return "redirect:/post/" + id;
    }
}
