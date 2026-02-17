package com.rentaroom.controller;

import com.rentaroom.model.User;
import com.rentaroom.service.RoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class HomeController {
    
    @Autowired
    private RoomService roomService;
    
    @GetMapping("/")
    public String home(Model model, HttpSession session,
                       @RequestParam(required = false) String location,
                       @RequestParam(required = false) BigDecimal maxPrice) {
        
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        if (location != null || maxPrice != null) {
            model.addAttribute("rooms", roomService.searchAvailableRooms(location, maxPrice));
        } else {
            model.addAttribute("rooms", roomService.findAllAvailable());
        }
        
        model.addAttribute("location", location);
        model.addAttribute("maxPrice", maxPrice);
        
        return "index";
    }
}
