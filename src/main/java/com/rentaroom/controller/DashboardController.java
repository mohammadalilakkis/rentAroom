package com.rentaroom.controller;

import com.rentaroom.model.HostProfile;
import com.rentaroom.model.User;
import com.rentaroom.service.BookingService;
import com.rentaroom.service.HostProfileService;
import com.rentaroom.service.RoomService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class DashboardController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private HostProfileService hostProfileService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        // Redirect admins to admin dashboard
        if (user.getRole() == User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        
        model.addAttribute("user", user);
        
        if (user.getRole() == User.Role.HOST) {
            model.addAttribute("rooms", roomService.findByHost(user));
            model.addAttribute("bookings", bookingService.findByRenter(user));
            // Check if host has a profile
            Optional<HostProfile> profile = hostProfileService.findByUser(user);
            model.addAttribute("hasProfile", profile.isPresent());
        } else {
            model.addAttribute("bookings", bookingService.findByRenter(user));
        }
        
        return "dashboard";
    }
    
    @GetMapping("/host/profile")
    public String showHostProfileForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.HOST) {
            return "redirect:/login";
        }
        
        Optional<HostProfile> existingProfile = hostProfileService.findByUser(user);
        HostProfile hostProfile = existingProfile.orElse(new HostProfile());
        hostProfile.setUser(user);
        
        model.addAttribute("hostProfile", hostProfile);
        return "host-profile";
    }
    
    @PostMapping("/host/profile")
    public String saveHostProfile(@Valid @ModelAttribute HostProfile hostProfile, BindingResult result, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.HOST) {
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            return "host-profile";
        }
        
        hostProfile.setUser(user);
        hostProfileService.createOrUpdate(hostProfile);
        model.addAttribute("message", "Profile saved successfully!");
        return "redirect:/dashboard";
    }
}
