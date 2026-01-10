package com.rentaroom.controller;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import com.rentaroom.service.BookingService;
import com.rentaroom.service.RoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private RoomService roomService;
    
    @PostMapping("/create")
    public String createBooking(@RequestParam Long roomId,
                               @RequestParam String checkIn,
                               @RequestParam String checkOut,
                               @RequestParam(required = false, defaultValue = "1") Integer beds,
                               HttpSession session,
                               Model model) {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        Optional<Room> room = roomService.findById(roomId);
        if (room.isEmpty()) {
            return "redirect:/";
        }
        
        try {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            
            Booking booking = bookingService.createBooking(room.get(), user, checkInDate, checkOutDate, beds);
            return "redirect:/bookings/" + booking.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("room", room.get());
            return "room-detail";
        }
    }
    
    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        Optional<Booking> booking = bookingService.findById(id);
        if (booking.isEmpty() || booking.get().getRenter().getId() != user.getId()) {
            return "redirect:/";
        }
        
        model.addAttribute("booking", booking.get());
        return "booking-detail";
    }
    
    @GetMapping("/my-bookings")
    public String myBookings(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("bookings", bookingService.findByRenter(user));
        return "my-bookings";
    }
    
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        Optional<Booking> booking = bookingService.findById(id);
        if (booking.isPresent() && booking.get().getRenter().getId() == user.getId()) {
            bookingService.cancelBooking(id);
        }
        
        return "redirect:/bookings/my-bookings";
    }
}
