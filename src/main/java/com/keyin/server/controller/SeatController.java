package com.keyin.server.controller;

import com.keyin.server.model.Seat;
import com.keyin.server.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<Seat>> getSeatsByScreen(@PathVariable Long screenId) {
        return ResponseEntity.ok(seatService.getSeatsByScreen(screenId));
    }

    @GetMapping("/screen/{screenId}/available")
    public ResponseEntity<List<Seat>> getAvailableSeatsByScreen(@PathVariable Long screenId) {
        return ResponseEntity.ok(seatService.getAvailableSeatsByScreen(screenId));
    }

    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.saveSeat(seat));
    }

    @PostMapping("/screen/{screenId}/bulk")
    public ResponseEntity<List<Seat>> createSeatsForScreen(
            @PathVariable Long screenId,
            @RequestBody List<Seat> seats) {
        List<Seat> createdSeats = seatService.createSeatsForScreen(screenId, seats);
        if (createdSeats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable Long id, @RequestBody Seat seat) {
        return seatService.updateSeat(id, seat)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Seat> updateSeatAvailability(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> availability) {
        Boolean available = availability.get("available");
        if (available == null) {
            return ResponseEntity.badRequest().build();
        }

        return seatService.updateSeatAvailability(id, available)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        return seatService.getSeatById(id)
                .map(seat -> {
                    seatService.deleteSeat(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}