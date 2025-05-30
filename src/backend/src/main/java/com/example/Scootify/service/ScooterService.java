package com.example.Scootify.service;

import com.example.Scootify.model.Scooter;
import com.example.Scootify.repository.ScooterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScooterService {
    
    @Autowired
    private ScooterRepository scooterRepository;

    public List<Scooter> findAvailableScooters(double lat, double lng, double radius) {
        // 查询滑板车逻辑，根据经纬度查找可用的滑板车，可以使用JPA查询或自定义查询
        return scooterRepository.findAvailableScooters(lat, lng, radius);
    }

    public void addScooter(Scooter scooter) {
        try {
            System.out.println("Saving scooter: " + scooter);
            scooterRepository.save(scooter);
            System.out.println("Scooter saved successfully");
        } catch (Exception e) {
            System.err.println("Error saving scooter: " + e.getMessage());
            throw e;
        }
    }

    // 其他的增、删、改查方法
}