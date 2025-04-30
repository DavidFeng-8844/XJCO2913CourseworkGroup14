package com.example.Scootify.service;

import com.example.Scootify.model.Booking;
import com.example.Scootify.model.Scooter;
import com.example.Scootify.model.User;
import com.example.Scootify.repository.BookingRepository;
import com.example.Scootify.repository.ScooterRepository;
import com.example.Scootify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository; // 用于查找用户

    @Autowired
    private ScooterRepository scooterRepository; // 用于查找滑板车

    public Booking createBooking(Long userId, String scooterId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("User not found")); // 查找用户
        Scooter scooter = scooterRepository.findById(scooterId)
                                            .orElseThrow(() -> new RuntimeException("Scooter not found")); // 查找滑板车
        
        Booking booking = new Booking();
        booking.setUser(user);    // 使用 setUser 方法
        booking.setScooter(scooter); // 使用 setScooter 方法
        booking.setStatus("active"); // 设置状态为 "active"

        return bookingRepository.save(booking); // 保存预订
    }

    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId); // 根据 ID 取消预订
    }

    
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId); // 查找用户的所有预订
    }
}