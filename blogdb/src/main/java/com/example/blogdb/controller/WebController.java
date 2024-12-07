package com.example.blogdb.controller;

import com.example.blogdb.model.Post;
import com.example.blogdb.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts/list";
    }

    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/form";
    }

    @PostMapping("/posts/new")
    public String createPost(@Valid @ModelAttribute Post post,
                             BindingResult result,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "posts/form";
        }

        postService.createPost(post);
        attributes.addFlashAttribute("success", "Post created successfully!");
        return "redirect:/";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        return postService.getPostById(id)
                .map(post -> {
                    model.addAttribute("post", post);
                    return "posts/view";
                })
                .orElse("redirect:/");
    }

    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        return postService.getPostById(id)
                .map(post -> {
                    model.addAttribute("post", post);
                    return "posts/form";
                })
                .orElse("redirect:/");
    }

    @PostMapping("/posts/{id}/edit")
    public String updatePost(@PathVariable Long id,
                             @Valid @ModelAttribute Post post,
                             BindingResult result,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "posts/form";
        }

        postService.updatePost(id, post);
        attributes.addFlashAttribute("success", "Post updated successfully!");
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes attributes) {
        postService.deletePost(id);
        attributes.addFlashAttribute("success", "Post deleted successfully!");
        return "redirect:/";
    }
}