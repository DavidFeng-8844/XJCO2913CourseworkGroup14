package com.example.Scootify.model;

import jakarta.persistence.*;

@Entity
@Table(name = "scooters")
public class Scooter {

    @Id
    private String id; // 滑板车的唯一标识符
    private String status; // 滑板车的状态，比如 "available", "in use", "maintenance"
    
    private double locationLat; // 纬度
    private double locationLng; // 经度

    // 构造函数
    public Scooter() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }
}