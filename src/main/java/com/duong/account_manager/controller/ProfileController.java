package com.duong.account_manager.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.duong.account_manager.entity.User;
import com.duong.account_manager.repository.UserRepository;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final Cloudinary cloudinary;

    public ProfileController(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder,
            Cloudinary cloudinary) {

        this.userRepository = userRepository;
        this.encoder = encoder;
        this.cloudinary = cloudinary;
    }

    @GetMapping("/profile")
    public String profile(
            Principal principal,
            Model model) {

        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(

            Principal principal,

            @RequestParam String username,

            @RequestParam(required = false)
            MultipartFile avatar,

            @RequestParam(required = false)
            String newPassword) throws IOException {

        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        user.setUsername(username);

        // Upload avatar lên Cloudinary
        if (avatar != null && !avatar.isEmpty()) {

            Map uploadResult =
                    cloudinary.uploader().upload(
                            avatar.getBytes(),
                            ObjectUtils.emptyMap());

            String imageUrl =
                    uploadResult.get("secure_url").toString();

            user.setAvatarUrl(imageUrl);
        }

        // Đổi mật khẩu
        if (newPassword != null &&
                !newPassword.isBlank()) {

            user.setPassword(
                    encoder.encode(newPassword));
        }

        userRepository.save(user);

        return "redirect:/dashboard";
    }
}