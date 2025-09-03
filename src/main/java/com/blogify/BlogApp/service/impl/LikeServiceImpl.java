package com.blogify.BlogApp.service.impl;

import com.blogify.BlogApp.dto.LikeDTO;
import com.blogify.BlogApp.entity.Like;
import com.blogify.BlogApp.entity.Post;
import com.blogify.BlogApp.entity.User;
import com.blogify.BlogApp.repository.LikeRepository;
import com.blogify.BlogApp.repository.PostRepository;
import com.blogify.BlogApp.repository.UserRepository;
import com.blogify.BlogApp.service.LikeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public LikeServiceImpl(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public LikeDTO likePost(Long userId,Long postId){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found."));
        Post post = postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found."));
        Like like = new Like(user,post);
        Like saved = likeRepository.save(like);

        LikeDTO dto = modelMapper.map(saved,LikeDTO.class);
        dto.setUserId(userId);
        dto.setPostId(postId);
        return dto;
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
        if (!likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new RuntimeException("Like not found");
        }
        likeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
