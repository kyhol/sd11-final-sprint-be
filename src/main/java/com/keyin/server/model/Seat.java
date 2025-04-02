package com.keyin.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    @JsonIgnoreProperties({"seats", "showtimes", "theater"}) // Prevent infinite recursion from Screen
    private Screen screen;

    @Column(name = "row_letter")
    private String rowLetter;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "seat_type")
    private String seatType;

    // Map "available" as a boolean with a TINYINT(1) column definition
    @Column(name = "available", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean available = true;

    public Seat() {
    }

    public Seat(Screen screen, String rowLetter, Integer seatNumber) {
        this.screen = screen;
        this.rowLetter = rowLetter;
        this.seatNumber = seatNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public String getRowLetter() {
        return rowLetter;
    }

    public void setRowLetter(String rowLetter) {
        this.rowLetter = rowLetter;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", row='" + rowLetter + '\'' +
                ", number=" + seatNumber +
                ", screenId=" + (screen != null ? screen.getId() : null) +
                '}';
    }
}
