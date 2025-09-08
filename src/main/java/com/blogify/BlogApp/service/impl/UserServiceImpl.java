package com.blogify.BlogApp.service.impl;

import com.blogify.BlogApp.dto.UserDTO;
import com.blogify.BlogApp.entity.User;
import com.blogify.BlogApp.repository.UserRepository;
import com.blogify.BlogApp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO updateUser(Long id,UserDTO dto){
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found."));
        if(dto.getName() !=null){
            user.setName(dto.getName());
        }
        if(dto.getBio() !=null){
            user.setBio(dto.getBio());
        }
        if (dto.getEmail() != null){
            user.setEmail(dto.getEmail());
        }

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser,UserDTO.class);
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO){
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new RuntimeException("Email already exists!");
        }
        User user = modelMapper.map(userDTO,User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if(user.getRoles() == null || user.getRoles().isEmpty()){
            user.setRoles(new HashSet<>());
            user.getRoles().add("ROLE_USER");
        }
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser,UserDTO.class);
    }

    @Override
    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found."));
        userRepository.delete(user);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(user -> modelMapper.map(user,UserDTO.class));
    }

    @Override
    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found."));
        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    public String login(String email,String password){
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Invalid Credentials."));
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid Credentials.");
        }
        return "Successfull Login";
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return modelMapper.map(user, UserDTO.class);
    }

}
