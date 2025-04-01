package com.keyin.server.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "screens")
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(name = "screen_number")
    private Integer screenNumber;

    private Integer capacity;

    @Column(name = "screen_type")
    private String screenType;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private List<ShowTime> showtimes = new ArrayList<>();

    // Default constructor
    public Screen() {
    }

    // Constructor with required fields
    public Screen(Theater theater, Integer screenNumber) {
        this.theater = theater;
        this.screenNumber = screenNumber;
    }

    // Constructor with common fields
    public Screen(Theater theater, Integer screenNumber, Integer capacity, String screenType) {
        this.theater = theater;
        this.screenNumber = screenNumber;
        this.capacity = capacity;
        this.screenType = screenType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public Integer getScreenNumber() {
        return screenNumber;
    }

    public void setScreenNumber(Integer screenNumber) {
        this.screenNumber = screenNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<ShowTime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<ShowTime> showtimes) {
        this.showtimes = showtimes;
    }

    // Helper method to add seat
    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setScreen(this);
    }

    @Override
    public String toString() {
        return "Screen{" +
                "id=" + id +
                ", screenNumber=" + screenNumber +
                ", screenType='" + screenType + '\'' +
                ", theaterId=" + (theater != null ? theater.getId() : null) +
                '}';
    }
}