package com.example.Scootify.controller;

import com.example.Scootify.model.Booking;
import com.example.Scootify.model.Scooter;
import com.example.Scootify.service.BookingService;
import com.example.Scootify.service.ScooterService;
import com.example.Scootify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        try {
            bookingService.cancelBooking(id);
            return ResponseEntity.ok("Booking canceled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel booking: " + e.getMessage());
        }
    }

    // 获取用户的所有预订
    @GetMapping("/get-bookings")
    public List<Booking> getUserBookings(@RequestParam Long userId) {   
        System.out.println("Received request to get bookings for user: " + userId);
        return bookingService.getUserBookings(userId);
    }

    // Extend booking duration
    // Extend booking duration
    @GetMapping("/extend-booking")
    public ResponseEntity<String> extendBooking(@RequestParam Long bookingId, @RequestParam int additionalHours) {
        System.out.println("Received request to extend booking: " + bookingId + " by " + additionalHours + " hours.");
        try {
            bookingService.extendBooking(bookingId, additionalHours);
            return ResponseEntity.ok("Booking extended successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to extend booking: " + e.getMessage());
        }
    }
}


// Removed GlobalExceptionHandler class from this file.