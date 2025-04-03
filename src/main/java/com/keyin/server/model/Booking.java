package com.keyin.server.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to ShowTime
    @ManyToOne(optional = false)
    @JoinColumn(name = "showtime_id")
    private ShowTime showTime;

    // Many-to-many link to seats in a separate join table
    @ManyToMany
    @JoinTable(name = "booking_seats",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id"))
    private List<Seat> seats = new ArrayList<>();

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime = LocalDateTime.now();

    // Random ticket/serial number
    @Column(name = "ticket_number", unique = true, nullable = false)
    private String ticketNumber;

    public Booking() {
        // Generate a random ticket number by default:
        // e.g. TICKET-XYZ12345
        this.ticketNumber = "TICKET-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }

    public Booking(ShowTime showTime, List<Seat> seats) {
        this(); // calls default constructor to init ticketNumber
        this.showTime = showTime;
        this.seats = seats;
        this.bookingTime = LocalDateTime.now();
    }

    // ============ Getters/Setters ============

    public Long getId() {
        return id;
    }

    public ShowTime getShowTime() {
        return showTime;
    }
    public void setShowTime(ShowTime showTime) {
        this.showTime = showTime;
    }

    public List<Seat> getSeats() {
        return seats;
    }
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
}
