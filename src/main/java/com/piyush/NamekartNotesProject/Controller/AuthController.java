package com.piyush.NamekartNotesProject.Controller;

import com.piyush.NamekartNotesProject.Entity.User;
import com.piyush.NamekartNotesProject.Service.UserService;
import com.piyush.NamekartNotesProject.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 🔹 Sign Up (Register User)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔹 Login (Generate JWT)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        return userService.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        //Generate JWT Token
                        String token = jwtUtil.generateToken(user.getUsername());
                        return ResponseEntity.ok("Bearer " + token);
                    } else {
                        return ResponseEntity.badRequest().body("Invalid credentials");
                    }
                })
                .orElse(ResponseEntity.badRequest().body("User not found"));
    }
}
