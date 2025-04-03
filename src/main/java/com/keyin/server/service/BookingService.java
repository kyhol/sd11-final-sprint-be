package com.keyin.server.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyin.server.model.Booking;
import com.keyin.server.model.Seat;
import com.keyin.server.model.ShowTime;
import com.keyin.server.repository.BookingRepository;
import com.keyin.server.repository.SeatRepository;
import com.keyin.server.repository.ShowTimeRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SeatRepository seatRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          ShowTimeRepository showTimeRepository,
                          SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.showTimeRepository = showTimeRepository;
        this.seatRepository = seatRepository;
    }

    public Booking createBooking(Long showTimeId, List<Long> seatIds) {
        // 1) Find ShowTime
        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new RuntimeException("ShowTime not found: " + showTimeId));

        // 2) Find seats
        List<Seat> seats = seatRepository.findAllById(seatIds);
        if (seats.isEmpty()) {
            throw new RuntimeException("No valid seats found for IDs: " + seatIds);
        }

        // 3) Check availability
        for (Seat seat : seats) {
            if (!Boolean.TRUE.equals(seat.getAvailable())) {
                throw new RuntimeException("Seat " + seat.getId() + " is already booked!");
            }
        }

        // 4) Create booking
        Booking booking = new Booking(showTime, seats);
        booking.setBookingTime(LocalDateTime.now());

        // 5) Save booking
        Booking saved = bookingRepository.save(booking);

        // 6) Mark seats as unavailable
        for (Seat seat : seats) {
            seat.setAvailable(false);
        }
        seatRepository.saveAll(seats);

        return saved;
    }

    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }
}
