package com.keyin.server.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType; // <-- For EAGER
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "showtimes")
public class ShowTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to Movie
    @ManyToOne(fetch = FetchType.EAGER) // or LAZY if you prefer, but usually EAGER is easier for direct JSON
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    // Link to Screen (which then links to Theater)
    // EAGER so 'screen' is always loaded with this ShowTime
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "screen_id", nullable = false)
    @JsonIgnoreProperties({"showtimes", "seats"}) // helps avoid infinite loops if needed
    private Screen screen;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "date")
    private LocalDate date;

    private BigDecimal price;

    public ShowTime() {
    }

    public ShowTime(Movie movie, Screen screen, LocalDateTime startTime) {
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.date = startTime != null ? startTime.toLocalDate() : null;
    }

    public ShowTime(Movie movie, Screen screen, LocalDateTime startTime,
                    LocalDateTime endTime, BigDecimal price) {
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = startTime != null ? startTime.toLocalDate() : null;
        this.price = price;
    }

    // GETTERS / SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        if (this.startTime != null) {
            this.date = this.startTime.toLocalDate();
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void calculateEndTime() {
        if (this.startTime != null && this.movie != null && this.movie.getDurationMinutes() != null) {
            this.endTime = this.startTime.plusMinutes(this.movie.getDurationMinutes());
        }
    }

    @Override
    public String toString() {
        return "ShowTime{" +
                "id=" + id +
                ", movieId=" + (movie != null ? movie.getId() : null) +
                ", screenId=" + (screen != null ? screen.getId() : null) +
                ", startTime=" + startTime +
                ", date=" + date +
                '}';
    }
}
