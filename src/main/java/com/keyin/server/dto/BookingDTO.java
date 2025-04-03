package com.keyin.server.dto;

import java.util.List;

public class BookingDTO {

    private Long showTimeId;
    private List<Long> seatIds;

    // The response fields
    private Long bookingId;
    private String bookingTime;
    private String ticketNumber;
    private List<Long> bookedSeatIds;

    public BookingDTO() {}

    public Long getShowTimeId() {
        return showTimeId;
    }
    public void setShowTimeId(Long showTimeId) {
        this.showTimeId = showTimeId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }
    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public Long getBookingId() {
        return bookingId;
    }
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingTime() {
        return bookingTime;
    }
    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public List<Long> getBookedSeatIds() {
        return bookedSeatIds;
    }
    public void setBookedSeatIds(List<Long> bookedSeatIds) {
        this.bookedSeatIds = bookedSeatIds;
    }
}
