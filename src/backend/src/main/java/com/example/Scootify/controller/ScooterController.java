package com.example.Scootify.controller;

import com.example.Scootify.model.Booking;
import com.example.Scootify.model.Scooter;
import com.example.Scootify.service.BookingService;
import com.example.Scootify.service.ScooterService;
import com.example.Scootify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScooterService scooterService;

    @Autowired
    private BookingService bookingService;

    // 获取可用滑板车
    @GetMapping("/get-scooters")
    public List<Scooter> getAvailableScooters(@RequestParam double lat, @RequestParam double lng, @RequestParam double radius) {
        System.out.println("Received parameters - lat: " + lat + ", lng: " + lng + ", radius: " + radius);
        return scooterService.findAvailableScooters(lat, lng, radius);
    }

    // 添加滑板车（管理员权限）
    @PostMapping("/add-scooter")
    public void addScooter(@RequestBody Scooter scooter) {
        System.out.println("Received scooter: " + scooter);
        scooterService.addScooter(scooter);
    }


    // 创建预订
    @GetMapping("/bookings")
    public Booking createBooking(
        @RequestParam Long userId,
        @RequestParam String scooterId,
        @RequestParam int durationInHours,
        @RequestParam String cardNumber
    ) {
        System.out.println("Received booking request: "
            + userId + ", " + scooterId + ", " + durationInHours + ", " + cardNumber);
    
        return bookingService.createBooking(userId, scooterId, durationInHours, cardNumber);
    }
    // 取消预订
    @DeleteMapping("/bookings/{id}")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }

    // 获取用户的所有预订
    @GetMapping("/get-bookings")
    public List<Booking> getUserBookings(@RequestParam Long userId) {
        return bookingService.getUserBookings(userId);
    }
}


// Removed GlobalExceptionHandler class from this file.