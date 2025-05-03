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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;


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
        System.out.println("Service Received booking request: "
            + userId + ", " + scooterId + ", " + durationInHours + ", " + cardNumber);
            User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                System.out.println("User not found: " + userId);
                return new RuntimeException("User not found");
            });
            Scooter scooter = scooterRepository.findById(scooterId)
            .orElseThrow(() -> {
                System.out.println("Scooter not found: " + scooterId);
                return new RuntimeException("Scooter not found");
            });

        if ("unavailable".equals(scooter.getStatus())) {
            throw new RuntimeException("Scooter is already unavailable.");
        }
                                            
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
        // if (!processPayment(cardNumber, cost)) {
        //     System.out.println("Payment failed for card: " + cardNumber);
        //     throw new RuntimeException("Payment failed");
        // }

        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.now()); // 设置开始时间
        booking.setEndTime(LocalDateTime.now().plusHours(durationInHours)); // 设置结束时间为1小时后
        booking.setUser(user);    // 使用 setUser 方法
        booking.setScooter(scooter); // 使用 setScooter 方法
        booking.setStatus("active"); // 设置状态为 "active"

        scooter.setStatus("unavailable"); // 设置滑板车状态为 "unavailable"
        scooterRepository.save(scooter); // 保存滑板车状态

        // 发送确认邮件
        // emailService.sendEmail(user.getEmail(), "Booking Confirmation",
        //     "Your booking for scooter " + scooterId + " is confirmed. Start time: " + booking.getStartTime());

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
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        Scooter scooter = booking.getScooter();
        scooter.setStatus("available"); // 设置滑板车状态为 "available"
        scooterRepository.save(scooter); // 保存滑板车状态
        bookingRepository.deleteById(bookingId); // 根据 ID 取消预订

    }

    public void extendBooking(Long bookingId, int additionalHours) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.checkAndUpdateStatus(); // 检查并更新预订状态
        LocalDateTime newEndTime = booking.getEndTime().plusHours(additionalHours); // 延长预订时间
        // set booking status to active
        booking.setStatus("active"); // 设置状态为 "active"
        booking.setEndTime(newEndTime); // 更新预订结束时间
        bookingRepository.save(booking); // 保存更新后的预订
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

    // @Scheduled(cron = "0 0 * * * ?") // 每小时执行一次
    // 每十秒执行一次
    @Scheduled(fixedRate = 1000) // 每十秒执行一次
    public void updateBookingStatuses() {
        List<Booking> bookings = bookingRepository.findAll();
        for (Booking booking : bookings) {
            booking.checkAndUpdateStatus(); // 检查并更新状态
            bookingRepository.save(booking); // 保存更新后的状态
        }
    }

    public Map<String, Double> calculateWeeklyIncomeByRentalOption() {
    List<Booking> bookings = bookingRepository.findAll();
    LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
    // print bookings
    System.out.println("Bookings in the last week: " + bookings.stream()
        .filter(booking -> booking.getStartTime().isAfter(oneWeekAgo))
        .collect(Collectors.toList()));
    // print oneWeekAgo
    return bookings.stream()
        .filter(booking -> booking.getStartTime().isAfter(oneWeekAgo))
        .collect(Collectors.groupingBy(
            booking -> {
                long duration = ChronoUnit.HOURS.between(booking.getStartTime(), booking.getEndTime());
                if (duration == 1) return "1 hour";
                else if (duration == 4) return "4 hours";
                else if (duration == 24) return "1 day";
                else if (duration == 168) return "1 week";
                else return "Other";
            },
            Collectors.summingDouble(booking -> {
                long duration = ChronoUnit.HOURS.between(booking.getStartTime(), booking.getEndTime());
                if (duration == 1) return 10;
                else if (duration == 4) return 30;
                else if (duration == 24) return 50;
                else if (duration == 168) return 200;
                else return 0;
            })
        ));
    }

    public Map<LocalDate, Double> calculateDailyIncomeOverWeek() {
        List<Booking> bookings = bookingRepository.findAll();
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        

        return bookings.stream()
            .filter(booking -> booking.getStartTime().isAfter(oneWeekAgo))
            .filter(booking -> ChronoUnit.HOURS.between(booking.getStartTime(), booking.getEndTime()) != 168) // Exclude 1-week hires
            .collect(Collectors.groupingBy(
                booking -> booking.getStartTime().toLocalDate(),
                Collectors.summingDouble(booking -> {
                    long duration = ChronoUnit.HOURS.between(booking.getStartTime(), booking.getEndTime());
                    if (duration == 1) return 10;
                    else if (duration == 4) return 30;
                    else if (duration == 24) return 50;
                    else return 0;
                })
            ));
    }
    
}