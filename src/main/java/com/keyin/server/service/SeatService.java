package com.keyin.server.service;

import com.keyin.server.model.Screen;
import com.keyin.server.model.Seat;
import com.keyin.server.repository.ScreenRepository;
import com.keyin.server.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository, ScreenRepository screenRepository) {
        this.seatRepository = seatRepository;
        this.screenRepository = screenRepository;
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    public List<Seat> getSeatsByScreen(Long screenId) {
        Optional<Screen> screen = screenRepository.findById(screenId);
        return screen.map(seatRepository::findByScreen).orElse(List.of());
    }

    public List<Seat> getAvailableSeatsByScreen(Long screenId) {
        Optional<Screen> screen = screenRepository.findById(screenId);
        return screen.map(s -> seatRepository.findByScreenAndAvailable(s, true)).orElse(List.of());
    }

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

    // Bulk operations for creating multiple seats at once for a screen
    public List<Seat> createSeatsForScreen(Long screenId, List<Seat> seats) {
        return screenRepository.findById(screenId)
                .map(screen -> {
                    seats.forEach(seat -> seat.setScreen(screen));
                    return seatRepository.saveAll(seats);
                })
                .orElse(List.of());
    }
}