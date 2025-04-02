package com.keyin.server.dto;

public class ScreenDTO {
    private Long id;
    private Long theaterId;   // to link back to Theater
    private Integer screenNumber;
    private Integer capacity;
    private String screenType;

    public ScreenDTO() {
    }

    // Optionally, add a constructor with all fields:
    // public ScreenDTO(Long id, Long theaterId, Integer screenNumber, Integer capacity, String screenType) { ... }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheaterId() {
        return theaterId;
    }
    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
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
}
