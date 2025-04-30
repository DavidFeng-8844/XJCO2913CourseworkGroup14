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

    public List<Scooter> findAvailableScooters(double lat, double lng) {
        // 查询滑板车逻辑，根据经纬度查找可用的滑板车，可以使用JPA查询或自定义查询
        return scooterRepository.findAvailableScooters(lat, lng);
    }

    public void addScooter(Scooter scooter) {
        scooterRepository.save(scooter);
    }

    // 其他的增、删、改查方法
}