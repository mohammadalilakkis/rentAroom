package com.rentaroom.controller;

import com.rentaroom.model.User;
import com.rentaroom.service.BookingService;
import com.rentaroom.service.RoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private BookingService bookingService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        if (user.getRole() == User.Role.HOST) {
            model.addAttribute("rooms", roomService.findByHost(user));
            model.addAttribute("bookings", bookingService.findByRenter(user));
        } else {
            model.addAttribute("bookings", bookingService.findByRenter(user));
        }
        
        return "dashboard";
    }
}
