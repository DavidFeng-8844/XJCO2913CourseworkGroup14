package com.example.Scootify.repository;

import com.example.Scootify.model.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScooterRepository extends JpaRepository<Scooter, String> {
    
    // 创建自定义查询，根据经纬度查找可用的滑板车
    @Query("SELECT s FROM Scooter s WHERE s.status = 'available' AND " +
           "FUNCTION('ST_Distance_Sphere', s.locationLat, s.locationLng, :lat, :lng) < 5000") // 5000米为例
    List<Scooter> findAvailableScooters(@Param("lat") double lat, @Param("lng") double lng);
}