package com.duong.account_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.duong.account_manager.entity.User;
import com.duong.account_manager.repository.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);

        // mã hóa password
        user.setPassword(
                encoder.encode(password));

        user.setRole("USER");

        if(userRepository.findByEmail(email).isPresent()){

            return "redirect:/register?error=email_exists";
        }

        userRepository.save(user);

        return "redirect:/login";
    }

}