package com.keyin.server.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keyin.server.dto.BookingDTO;
import com.keyin.server.model.Booking;
import com.keyin.server.model.Seat;
import com.keyin.server.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO request) {
        // request has showTimeId, seatIds
        Booking booking = bookingService.createBooking(request.getShowTimeId(), request.getSeatIds());

        // Build response
        BookingDTO response = new BookingDTO();
        response.setBookingId(booking.getId());
        response.setShowTimeId(booking.getShowTime().getId());
        response.setBookingTime(
                booking.getBookingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
        response.setTicketNumber(booking.getTicketNumber());

        // Return seat IDs that were booked
        List<Long> bookedSeatIds = booking.getSeats().stream()
                .map(Seat::getId)
                .collect(Collectors.toList());
        response.setBookedSeatIds(bookedSeatIds);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(booking -> {
                    BookingDTO dto = new BookingDTO();
                    dto.setBookingId(booking.getId());
                    dto.setShowTimeId(booking.getShowTime().getId());
                    dto.setBookingTime(
                        booking.getBookingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    );
                    dto.setTicketNumber(booking.getTicketNumber());
                    List<Long> seatIds = booking.getSeats().stream()
                            .map(Seat::getId)
                            .collect(Collectors.toList());
                    dto.setBookedSeatIds(seatIds);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
