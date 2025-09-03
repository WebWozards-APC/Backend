package com.blogify.BlogApp.controller;

import com.blogify.BlogApp.dto.CreatePostRequest;
import com.blogify.BlogApp.dto.PostDTO;
import com.blogify.BlogApp.dto.UpdatePostRequest;
import com.blogify.BlogApp.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> createPost(@RequestParam Long userId, @RequestParam String title, @RequestParam String content, @RequestPart(value = "image",required = false)MultipartFile image){
        CreatePostRequest req = new CreatePostRequest();
        req.setTitle(title);
        req.setContent(content);

        PostDTO dto = postService.createPost(userId,req,image);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getById(@PathVariable Long postId){
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long postId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String imgUrl,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        UpdatePostRequest req = new UpdatePostRequest();
        req.setTitle(title);
        req.setContent(content);
        req.setImgUrl(imgUrl);

        PostDTO updated = postService.updatePost(postId, req, image);
        return ResponseEntity.ok(updated);
    }


}
