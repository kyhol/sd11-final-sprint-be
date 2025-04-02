package com.keyin.server.dto;

public class ScreenDTO {
    private Long id;
    private String screenType;
    private Long theaterId;  // Link to the Theater, if needed

    // Constructors
    public ScreenDTO() {
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getScreenType() {
        return screenType;
    }
    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }
    public Long getTheaterId() {
        return theaterId;
    }
    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }
}
