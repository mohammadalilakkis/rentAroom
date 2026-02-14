package com.rentaroom.controller;

import com.rentaroom.model.Booking;
import com.rentaroom.model.Payment;
import com.rentaroom.model.Room;
import com.rentaroom.model.User;
import com.rentaroom.service.AdminService;
import com.rentaroom.service.BookingService;
import com.rentaroom.service.RoomService;
import com.rentaroom.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private BookingService bookingService;
    
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == User.Role.ADMIN;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // #region agent log
        try {
            java.nio.file.Path logPath = java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log");
            java.nio.file.Files.createDirectories(logPath.getParent());
            java.io.FileWriter fw = new java.io.FileWriter(logPath.toString(), true);
            fw.write("{\"location\":\"AdminController.java:42\",\"message\":\"dashboard entry\",\"data\":{},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A\"}\n");
            fw.close();
        } catch (Exception e) {}
        // #endregion
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        try {
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"location\":\"AdminController.java:52\",\"message\":\"before getOverviewStats\",\"data\":{},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A\"}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            Map<String, Object> overviewStats = adminService.getOverviewStats();
            model.addAttribute("overviewStats", overviewStats);
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"location\":\"AdminController.java:58\",\"message\":\"after getOverviewStats\",\"data\":{},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A\"}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            
            Map<String, Object> userAnalytics = adminService.getUserAnalytics();
            model.addAttribute("userAnalytics", userAnalytics);
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"location\":\"AdminController.java:64\",\"message\":\"after getUserAnalytics\",\"data\":{},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A\"}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            
            Map<String, Object> roomAnalytics = adminService.getRoomAnalytics();
            model.addAttribute("roomAnalytics", roomAnalytics);
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"location\":\"AdminController.java:70\",\"message\":\"after getRoomAnalytics\",\"data\":{},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A\"}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            
            Map<String, Object> bookingAnalytics = adminService.getBookingAnalytics();
            model.addAttribute("bookingAnalytics", bookingAnalytics);
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"location\":\"AdminController.java:76\",\"message\":\"after getBookingAnalytics\",\"data\":{},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A\"}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            
            Map<String, Object> topPerformers = adminService.getTopPerformers();
            model.addAttribute("topPerformers", topPerformers);
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"location\":\"AdminController.java:82\",\"message\":\"after getTopPerformers\",\"data\":{},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A\"}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            
            Map<String, Object> financialAnalytics = adminService.getFinancialAnalytics();
            model.addAttribute("financialAnalytics", financialAnalytics);
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"location\":\"AdminController.java:88\",\"message\":\"after getFinancialAnalytics, before return\",\"data\":{},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A,B\"}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            
            return "admin/dashboard";
        } catch (Exception e) {
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                String msg = e.getMessage() != null ? e.getMessage().replace("\"", "'") : "null";
                String causeMsg = e.getCause() != null && e.getCause().getMessage() != null ? e.getCause().getMessage().replace("\"", "'") : "null";
                fw.write("{\"location\":\"AdminController.java:catch\",\"message\":\"exception\",\"data\":{\"type\":\"" + e.getClass().getName() + "\",\"message\":\"" + msg + "\",\"causeType\":\"" + (e.getCause() != null ? e.getCause().getClass().getName() : "null") + "\",\"causeMessage\":\"" + causeMsg + "\"},\"timestamp\":" + System.currentTimeMillis() + ",\"hypothesisId\":\"A,B,C,D,E\"}\n");
                fw.close();
            } catch (Exception ex) {}
            // #endregion
            throw e;
        }
    }
    
    @GetMapping("/users")
    public String manageUsers(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/users";
    }
    
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        // Prevent deleting yourself
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null && currentUser.getId().equals(id)) {
            model.addAttribute("error", "You cannot delete your own account");
            model.addAttribute("users", adminService.getAllUsers());
            return "admin/users";
        }
        
        // Note: In production, you'd want to handle cascading deletes properly
        // For now, we'll just redirect - actual deletion would require a UserRepository.delete method
        return "redirect:/admin/users?message=user_deletion_not_implemented";
    }
    
    @GetMapping("/rooms")
    public String manageRooms(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        model.addAttribute("rooms", adminService.getAllRooms());
        return "admin/rooms";
    }
    
    @GetMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id, HttpSession session) {
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\",\"location\":\"AdminController.java:115\",\"message\":\"deleteRoom entry\",\"data\":{\"roomId\":\"" + id + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
            fw.close();
        } catch (Exception e) {}
        // #endregion
        
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        // #region agent log
        try {
            java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
            fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\",\"location\":\"AdminController.java:122\",\"message\":\"before deleteById call\",\"data\":{\"roomId\":\"" + id + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
            fw.close();
        } catch (Exception e) {}
        // #endregion
        
        try {
            roomService.deleteById(id);
            
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A\",\"location\":\"AdminController.java:130\",\"message\":\"deleteById succeeded\",\"data\":{\"roomId\":\"" + id + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
                fw.close();
            } catch (Exception e) {}
            // #endregion
            
            return "redirect:/admin/rooms?message=room_deleted";
        } catch (Exception e) {
            // #region agent log
            try {
                java.io.FileWriter fw = new java.io.FileWriter(java.nio.file.Paths.get(System.getProperty("user.dir"), ".cursor", "debug.log").toString(), true);
                fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A,B,C,D\",\"location\":\"AdminController.java:137\",\"message\":\"deleteById exception\",\"data\":{\"roomId\":\"" + id + "\",\"exceptionType\":\"" + e.getClass().getName() + "\",\"exceptionMessage\":\"" + (e.getMessage() != null ? e.getMessage().replace("\"", "'") : "null") + "\",\"hasCause\":\"" + (e.getCause() != null) + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
                if (e.getCause() != null) {
                    fw.write("{\"sessionId\":\"debug-session\",\"runId\":\"run1\",\"hypothesisId\":\"A,B,C,D\",\"location\":\"AdminController.java:140\",\"message\":\"exception cause\",\"data\":{\"causeType\":\"" + e.getCause().getClass().getName() + "\",\"causeMessage\":\"" + (e.getCause().getMessage() != null ? e.getCause().getMessage().replace("\"", "'") : "null") + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n");
                }
                fw.close();
            } catch (Exception ex) {}
            // #endregion
            
            return "redirect:/admin/rooms?error=delete_failed";
        }
    }
    
    @GetMapping("/bookings")
    public String manageBookings(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        model.addAttribute("bookings", adminService.getAllBookings());
        return "admin/bookings";
    }
    
    @GetMapping("/bookings/confirm/{id}")
    public String confirmBooking(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        bookingService.confirmBooking(id);
        return "redirect:/admin/bookings?message=booking_confirmed";
    }
    
    @GetMapping("/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        bookingService.cancelBooking(id);
        return "redirect:/admin/bookings?message=booking_cancelled";
    }
    
    @GetMapping("/payments")
    public String managePayments(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        
        model.addAttribute("payments", adminService.getAllPayments());
        return "admin/payments";
    }
}
