package com.blogify.BlogApp.service;

import com.blogify.BlogApp.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO updateUser(Long id,UserDTO userDTO);
    void deleteUser(Long id);
    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO getUserById(Long userId);
    String login(String email,String password);
}
