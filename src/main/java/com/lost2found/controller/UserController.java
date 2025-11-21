package com.lost2found.controller;

import com.lost2found.model.User;
import com.lost2found.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model) {

        if (userRepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "❌ Username already exists!");
            return "register";
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "❌ Email already registered!");
            return "register";
        }
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "❌ Passwords do not match!");
            return "register";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
        model.addAttribute("message", "✅ Registration Successful! You can now log in.");
        return "login";
    }
}
