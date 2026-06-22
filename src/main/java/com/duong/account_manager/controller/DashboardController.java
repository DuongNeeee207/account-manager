package com.duong.account_manager.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.duong.account_manager.entity.User;
import com.duong.account_manager.repository.UserRepository;

@Controller
public class DashboardController {

    private final UserRepository userRepository;

    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Principal principal,
            Model model) {

        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        model.addAttribute("user", user);

        model.addAttribute(
                "totalUsers",
                userRepository.count());

        return "dashboard";
    }
}