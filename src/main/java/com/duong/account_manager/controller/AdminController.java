package com.duong.account_manager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.duong.account_manager.entity.User;
import com.duong.account_manager.repository.UserRepository;

@Controller
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/admin")
    public String admin(Model model) {

        List<User> users = userRepository.findAll();

        model.addAttribute("users", users);

        return "admin";
    }
}