package com.keyin.server.controller;

import com.keyin.server.dto.SeatDTO;
import com.keyin.server.model.Seat;
import com.keyin.server.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<SeatDTO>> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        List<SeatDTO> dtos = seats.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id)
                .map(seat -> ResponseEntity.ok(toDTO(seat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<SeatDTO>> getSeatsByScreen(@PathVariable Long screenId) {
        List<Seat> seats = seatService.getSeatsByScreen(screenId);
        List<SeatDTO> dtos = seats.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/screen/{screenId}/available")
    public ResponseEntity<List<SeatDTO>> getAvailableSeatsByScreen(@PathVariable Long screenId) {
        List<Seat> seats = seatService.getAvailableSeatsByScreen(screenId);
        List<SeatDTO> dtos = seats.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<SeatDTO> createSeat(@RequestBody SeatDTO dto) {
        Seat seatEntity = toEntity(dto);
        Seat saved = seatService.saveSeat(seatEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @PostMapping("/screen/{screenId}/bulk")
    public ResponseEntity<List<SeatDTO>> createSeatsForScreen(
            @PathVariable Long screenId,
            @RequestBody List<SeatDTO> seatDTOs) {
        List<Seat> seatsToCreate = seatDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        List<Seat> createdSeats = seatService.createSeatsForScreen(screenId, seatsToCreate);

        if (createdSeats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<SeatDTO> createdDTOs = createdSeats.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDTO> updateSeat(@PathVariable Long id, @RequestBody SeatDTO dto) {
        return seatService.updateSeat(id, toEntity(dto))
                .map(updated -> ResponseEntity.ok(toDTO(updated)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/showtime/{showtimeId}/available")
    public ResponseEntity<List<Seat>> getSeatsByShowtime(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(seatService.getSeatsByShowtime(showtimeId));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<SeatDTO> updateSeatAvailability(
            @PathVariable Long id,
            @RequestBody Boolean available) {
        return seatService.updateSeatAvailability(id, available)
                .map(updated -> ResponseEntity.ok(toDTO(updated)))
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

    private SeatDTO toDTO(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setId(seat.getId());
        if (seat.getScreen() != null) {
            dto.setScreenId(seat.getScreen().getId());
        }
        dto.setRowLetter(seat.getRowLetter());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setSeatType(seat.getSeatType());
        dto.setAvailable(seat.getAvailable());
        return dto;
    }

    private Seat toEntity(SeatDTO dto) {
        Seat seat = new Seat();
        seat.setRowLetter(dto.getRowLetter());
        seat.setSeatNumber(dto.getSeatNumber());
        seat.setSeatType(dto.getSeatType());
        seat.setAvailable(dto.getAvailable());
        return seat;
    }
}
