package com.example.Scootify.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; // 关联合同的用户

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scooter_id")
    @JsonIgnore
    private Scooter scooter; // 关联合同的滑板车

    @JsonProperty("scooter_id")
    public String getScooterId() {
        return scooter != null ? scooter.getId() : null;
    }

    @JsonProperty("status")
    private String status;
    @JsonProperty("start_time")
    private LocalDateTime startTime; // 预订开始时间
    @JsonProperty("end_time")
    private LocalDateTime endTime; // 预订结束时间

    // 构造函数
    public Booking() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
}
}