package com.keyin.server.dto;

public class SeatDTO {
    private Long id;
    private String seatNumber;
    private Boolean available;
    private Long screenId; // Link to the Screen

    // Constructors
    public SeatDTO() {
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    public Boolean getAvailable() {
        return available;
    }
    public void setAvailable(Boolean available) {
        this.available = available;
    }
    public Long getScreenId() {
        return screenId;
    }
    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }
}
