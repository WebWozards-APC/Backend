package com.blogify.BlogApp.service.impl;

import com.blogify.BlogApp.dto.CreatePostRequest;
import com.blogify.BlogApp.dto.PostDTO;
import com.blogify.BlogApp.dto.UpdatePostRequest;
import com.blogify.BlogApp.entity.Post;
import com.blogify.BlogApp.entity.User;
import com.blogify.BlogApp.repository.PostRepository;
import com.blogify.BlogApp.repository.UserRepository;
import com.blogify.BlogApp.service.PostService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, Cloudinary cloudinary, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
        this.modelMapper = modelMapper;
    }

    // Business Logic for creating a blog:
    @Override
    public PostDTO createPost(Long userId, CreatePostRequest request, MultipartFile image){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        String imgUrl = null;
        if(image !=null && !image.isEmpty()){
            try{
                Map uploadResult = cloudinary.uploader().upload(
                        image.getBytes(),
                        ObjectUtils.asMap("folder","blog_posts")
                );
                imgUrl = (String) uploadResult.get("secure_url");

            } catch (IOException e) {
                throw new RuntimeException("Image upload failed",e);
            }
        }

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImgUrl(imgUrl);
        post.setUser(user);

        Post saved = postRepository.save(post);
        return mapPostToDTO(saved);
    }

    // Business logic for getting all in infinite scroll manner(for improving performance):
    @Override
    public Page<PostDTO> getAllPosts(Pageable pageable){
        return postRepository.findAll(pageable).map(this::mapPostToDTO);
    }

    // Business logic for getting a particular blog using its id:
    @Override
    public PostDTO getPostById(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("Post not found"));
        return mapPostToDTO(post);
    }

    // Business logic for deleting a particular blog:(Need to do improvements later.):
    @Override
    public void deletePost(Long postId){
        if(!postRepository.existsById(postId)){
            throw new RuntimeException("Post not found");
        }
        postRepository.deleteById(postId);
    }
     private PostDTO mapPostToDTO(Post post){
        PostDTO dto = modelMapper.map(post,PostDTO.class);
        if(post.getUser() != null){
            dto.setUserId(post.getUser().getId());
        }
         dto.setCreatedAt(post.getCreatedAt());
         return dto;
     }

    @Override
    public PostDTO updatePost(Long postId, UpdatePostRequest request, MultipartFile image) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Update only provided fields
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            post.setTitle(request.getTitle());
        }
        if (request.getContent() != null && !request.getContent().isEmpty()) {
            post.setContent(request.getContent());
        }

        // Handle new image upload
        if (image != null && !image.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        image.getBytes(),
                        ObjectUtils.asMap("folder", "blog_posts")
                );
                String imgUrl = (String) uploadResult.get("secure_url");
                post.setImgUrl(imgUrl);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        } else if (request.getImgUrl() != null && !request.getImgUrl().isEmpty()) {
            // If user provided direct URL instead of file
            post.setImgUrl(request.getImgUrl());
        }
        // ⚡️ else do nothing → old imgUrl stays

        Post updated = postRepository.save(post);
        return mapPostToDTO(updated);
    }

    @Override
    public Page<PostDTO> getPostsByUser(Long userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable)
                .map(post -> {
                    PostDTO dto = new PostDTO();
                    dto.setId(post.getId());
                    dto.setTitle(post.getTitle());
                    dto.setContent(post.getContent());
                    dto.setImgUrl(post.getImgUrl());
                    dto.setCreatedAt(post.getCreatedAt());
                    dto.setUserId(post.getUser().getId());
                    return dto;
                });
    }


}
