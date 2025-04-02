package com.keyin.server.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowTimeDTO {
    private Long id;
    private LocalDate showDate;
    private LocalTime showTime;
    private Long movieId;   // Link to the Movie
    private Long screenId;  // Link to the Screen

    // Constructors
    public ShowTimeDTO() {
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getShowDate() {
        return showDate;
    }
    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }
    public LocalTime getShowTime() {
        return showTime;
    }
    public void setShowTime(LocalTime showTime) {
        this.showTime = showTime;
    }
    public Long getMovieId() {
        return movieId;
    }
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    public Long getScreenId() {
        return screenId;
    }
    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }
}

