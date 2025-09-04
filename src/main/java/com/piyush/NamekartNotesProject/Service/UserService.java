package com.piyush.NamekartNotesProject.Service;

import com.piyush.NamekartNotesProject.Entity.User;
import com.piyush.NamekartNotesProject.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    // register a new user
    public User registerUser(User user){
        if(userRepository.existsByUsername(user.getUsername())){
            throw  new RuntimeException("Username Already Exist");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email Already Exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

     // find user by username
     public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }


}
