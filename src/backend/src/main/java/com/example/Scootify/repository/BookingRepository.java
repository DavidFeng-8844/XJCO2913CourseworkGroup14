package com.example.Scootify.repository;

import com.example.Scootify.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId); // 根据用户ID查询预订
}