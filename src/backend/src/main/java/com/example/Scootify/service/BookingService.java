package com.example.Scootify.service;

import com.example.Scootify.model.Booking;
import com.example.Scootify.model.Scooter;
import com.example.Scootify.model.User;
import com.example.Scootify.repository.BookingRepository;
import com.example.Scootify.repository.ScooterRepository;
import com.example.Scootify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository; // 用于查找用户

    @Autowired
    private ScooterRepository scooterRepository; // 用于查找滑板车

    @Autowired
    private EmailService emailService; // 用于发送邮件

    public Booking createBooking(Long userId, String scooterId, int durationInHours, String cardNumber) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("User not found")); // 查找用户
        Scooter scooter = scooterRepository.findById(scooterId)
                                            .orElseThrow(() -> new RuntimeException("Scooter not found")); // 查找滑板车
        // 1小时	$10
        // 4小时	$30
        //1天	$50
        // 1周	$200
        // calculate price based on duration
        double cost = 0;
        if (durationInHours == 1) {
            cost = 10;
        } else if (durationInHours == 4) {
            cost = 30;
        } else if (durationInHours == 24) {
            cost = 50;
        } else if (durationInHours == 168) {
            cost = 200;
        } else {
            throw new RuntimeException("Invalid duration");
        }
        // 处理支付
        if (!processPayment(cardNumber, cost)) {
            throw new RuntimeException("Payment failed");
        }

        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.now()); // 设置开始时间
        booking.setEndTime(LocalDateTime.now().plusHours(durationInHours)); // 设置结束时间为1小时后
        booking.setUser(user);    // 使用 setUser 方法
        booking.setScooter(scooter); // 使用 setScooter 方法
        booking.setStatus("active"); // 设置状态为 "active"

        scooter.setStatus("unavailable"); // 设置滑板车状态为 "unavailable"
        scooterRepository.save(scooter); // 保存滑板车状态

        // 发送确认邮件
        emailService.sendEmail(user.getEmail(), "Booking Confirmation",
            "Your booking for scooter " + scooterId + " is confirmed. Start time: " + booking.getStartTime());

        return bookingRepository.save(booking); // 保存预订
    }

    @Scheduled(fixedRate = 60000) // 每分钟检查一次
    public void checkBookingStatus() {
        List<Booking> bookings = bookingRepository.findAll(); // 查找所有预订
        for (Booking booking : bookings) {
            if (booking.getStatus().equals("active")) {
                // 检查预订是否过期，如果过期则更新状态
                if (booking.getEndTime().isBefore(LocalDateTime.now())) {
                    booking.setStatus("expired"); // 设置状态为 "expired"
                    bookingRepository.save(booking); // 保存更新后的预订
                }
            }
        }
    }

    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId); // 根据 ID 取消预订
    }

    
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId); // 查找用户的所有预订
    }

    public boolean processPayment(String cardNumber, double amount) {
        // Simulate payment processing
        if (cardNumber != null && cardNumber.length() == 16) {
            System.out.println("Payment of $" + amount + " processed successfully for card: " + cardNumber);
            return true;
        } else {
            throw new RuntimeException("Invalid card details");
        }
    }
}