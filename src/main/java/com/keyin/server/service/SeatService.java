package com.keyin.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyin.server.model.Screen;
import com.keyin.server.model.Seat;
import com.keyin.server.model.ShowTime;
import com.keyin.server.repository.ScreenRepository;
import com.keyin.server.repository.SeatRepository;
import com.keyin.server.repository.ShowTimeRepository;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final ShowTimeRepository showTimeRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository,
                       ScreenRepository screenRepository,
                       ShowTimeRepository showTimeRepository) {
        this.seatRepository = seatRepository;
        this.screenRepository = screenRepository;
        this.showTimeRepository = showTimeRepository;
    }

    // =============== MAIN GET METHODS ===============
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    // Get seats by screen ID (no filter)
    public List<Seat> getSeatsByScreen(Long screenId) {
        return screenRepository.findById(screenId)
                .map(seatRepository::findByScreen)
                .orElse(List.of());
    }

    // Get only available seats for a screen
    public List<Seat> getAvailableSeatsByScreen(Long screenId) {
        return screenRepository.findById(screenId)
                .map(s -> seatRepository.findByScreenAndAvailable(s, true))
                .orElse(List.of());
    }

    // =============== SHOWTIME-BASED GET ===============
    public List<Seat> getSeatsByShowtime(Long showtimeId) {
        // 1) Fetch the showtime by ID
        Optional<ShowTime> showTimeOpt = showTimeRepository.findById(showtimeId);
        if (showTimeOpt.isEmpty()) {
            // If showtime doesn't exist, return empty list
            return List.of();
        }
        // 2) Retrieve the screen from that showtime
        ShowTime showTime = showTimeOpt.get();
        Screen screen = showTime.getScreen();
        // 3) Return seats from that screen (only those that are available)
        return seatRepository.findByScreenAndAvailable(screen, true);
    }

    // =============== CREATE & UPDATE METHODS ===============
    public Seat saveSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Optional<Seat> updateSeat(Long id, Seat seatDetails) {
        return seatRepository.findById(id)
                .map(existingSeat -> {
                    if (seatDetails.getRowLetter() != null) {
                        existingSeat.setRowLetter(seatDetails.getRowLetter());
                    }
                    if (seatDetails.getSeatNumber() != null) {
                        existingSeat.setSeatNumber(seatDetails.getSeatNumber());
                    }
                    if (seatDetails.getSeatType() != null) {
                        existingSeat.setSeatType(seatDetails.getSeatType());
                    }
                    if (seatDetails.getAvailable() != null) {
                        existingSeat.setAvailable(seatDetails.getAvailable());
                    }
                    if (seatDetails.getScreen() != null) {
                        existingSeat.setScreen(seatDetails.getScreen());
                    }
                    return seatRepository.save(existingSeat);
                });
    }

    public Optional<Seat> updateSeatAvailability(Long id, Boolean available) {
        return seatRepository.findById(id)
                .map(existingSeat -> {
                    existingSeat.setAvailable(available);
                    return seatRepository.save(existingSeat);
                });
    }

    public void deleteSeat(Long id) {
        seatRepository.deleteById(id);
    }

    // =============== BULK CREATE FOR A SCREEN ===============
    public List<Seat> createSeatsForScreen(Long screenId, List<Seat> seats) {
        return screenRepository.findById(screenId)
                .map(screen -> {
                    seats.forEach(seat -> seat.setScreen(screen));
                    return seatRepository.saveAll(seats);
                })
                .orElse(List.of());
    }
}
