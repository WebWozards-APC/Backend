package com.blogify.BlogApp.controller;

import com.blogify.BlogApp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    @Autowired
    private PostRepository postRepository;

    // For admin -> total blogs in system
    @GetMapping("/admin")
    public Map<String, Object> getAdminDashboard() {
        long totalBlogs = postRepository.count();
        Map<String, Object> response = new HashMap<>();
        response.put("totalBlogs", totalBlogs);
        return response;
    }

    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserDashboard(@PathVariable Long userId) {
        long userBlogs = postRepository.countByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("totalBlogs", userBlogs);
        return response;
    }
}
