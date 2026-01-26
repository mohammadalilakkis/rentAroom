package com.rentaroom.controller;

import com.rentaroom.model.User;
import com.rentaroom.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/register")
    public String showRegisterForm(Model model, HttpSession session) {
        model.addAttribute("user", new User());
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("isEditor", currentUser != null && currentUser.getRole() == User.Role.ADMIN);
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            User currentUser = (User) session.getAttribute("user");
            model.addAttribute("isEditor", currentUser != null && currentUser.getRole() == User.Role.ADMIN);
            return "register";
        }
        
        // Prevent non-admins from registering as admin
        User currentUser = (User) session.getAttribute("user");
        if (user.getRole() == User.Role.ADMIN && (currentUser == null || currentUser.getRole() != User.Role.ADMIN)) {
            model.addAttribute("error", "Admin registration is only available to existing administrators.");
            model.addAttribute("isEditor", false);
            user.setRole(User.Role.RENTER); // Reset to default
            return "register";
        }
        
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("isEditor", currentUser != null && currentUser.getRole() == User.Role.ADMIN);
            return "register";
        }
        
        userService.save(user);
        return "redirect:/login?registered=true";
    }
    
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String registered, Model model) {
        if (registered != null) {
            model.addAttribute("message", "Registration successful! Please login.");
        }
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        
        if (userService.authenticate(email, password)) {
            User user = userService.findByEmail(email).orElse(null);
            if (user != null) {
                session.setAttribute("user", user);
                return "redirect:/";
            }
        }
        
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
