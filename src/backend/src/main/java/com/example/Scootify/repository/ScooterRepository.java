package com.example.Scootify.repository;

import com.example.Scootify.model.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScooterRepository extends JpaRepository<Scooter, String> {
    
    // 创建自定义查询，根据经纬度查找可用的滑板车
    @Query("SELECT s FROM Scooter s WHERE s.status = 'available' AND " +
       "(6371 * acos(cos(radians(:lat)) * cos(radians(s.locationLat)) * cos(radians(s.locationLng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(s.locationLat)))) <= :radius")
List<Scooter> findAvailableScooters(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius);
}
