package com.rentaroom.controller;

import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import com.rentaroom.service.RoomService;
import com.rentaroom.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public String viewRoom(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Room> room = roomService.findById(id);
        if (room.isEmpty()) {
            return "redirect:/";
        }
        
        model.addAttribute("room", room.get());
        model.addAttribute("user", session.getAttribute("user"));
        return "room-detail";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.HOST) {
            return "redirect:/";
        }
        
        model.addAttribute("room", new Room());
        return "room-form";
    }
    
    @PostMapping("/create")
    public String createRoom(@Valid @ModelAttribute Room room, BindingResult result, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.HOST) {
            return "redirect:/";
        }
        
        if (result.hasErrors()) {
            return "room-form";
        }
        
        room.setHost(user);
        roomService.save(room);
        return "redirect:/dashboard";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Optional<Room> room = roomService.findById(id);
        if (room.isEmpty() || room.get().getHost().getId() != user.getId()) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("room", room.get());
        return "room-form";
    }
    
    @PostMapping("/edit/{id}")
    public String updateRoom(@PathVariable Long id, @Valid @ModelAttribute Room room, BindingResult result, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Optional<Room> existingRoom = roomService.findById(id);
        if (existingRoom.isEmpty() || existingRoom.get().getHost().getId() != user.getId()) {
            return "redirect:/dashboard";
        }
        
        if (result.hasErrors()) {
            return "room-form";
        }
        
        room.setId(id);
        room.setHost(user);
        roomService.save(room);
        return "redirect:/dashboard";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Optional<Room> room = roomService.findById(id);
        if (room.isPresent() && room.get().getHost().getId() == user.getId()) {
            roomService.deleteById(id);
        }
        
        return "redirect:/dashboard";
    }
}
