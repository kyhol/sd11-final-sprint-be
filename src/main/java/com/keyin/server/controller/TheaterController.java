package com.keyin.server.controller;

import com.keyin.server.dto.TheaterDTO;
import com.keyin.server.model.Theater;
import com.keyin.server.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/theaters")
@CrossOrigin(origins = "*")
public class TheaterController {

    private final TheaterService theaterService;

    @Autowired
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    // --------------------------------------
    // GET All Theaters
    // --------------------------------------
    @GetMapping
    public ResponseEntity<List<TheaterDTO>> getAllTheaters() {
        List<Theater> theaters = theaterService.getAllTheaters();
        List<TheaterDTO> dtos = theaters.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --------------------------------------
    // GET Theater by ID
    // --------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<TheaterDTO> getTheaterById(@PathVariable Long id) {
        return theaterService.getTheaterById(id)
                .map(theater -> ResponseEntity.ok(toDTO(theater)))
                .orElse(ResponseEntity.notFound().build());
    }

    // --------------------------------------
    // GET Theaters by City
    // --------------------------------------
    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheaterDTO>> getTheatersByCity(@PathVariable String city) {
        List<Theater> theaters = theaterService.getTheatersByCity(city);
        List<TheaterDTO> dtos = theaters.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --------------------------------------
    // Search Theaters by Name
    // --------------------------------------
    @GetMapping("/search")
    public ResponseEntity<List<TheaterDTO>> searchTheaters(@RequestParam String name) {
        List<Theater> theaters = theaterService.searchTheaters(name);
        List<TheaterDTO> dtos = theaters.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // --------------------------------------
    // CREATE Theater
    // --------------------------------------
    @PostMapping
    public ResponseEntity<TheaterDTO> createTheater(@RequestBody TheaterDTO dto) {
        Theater entity = toEntity(dto);
        Theater saved = theaterService.saveTheater(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // --------------------------------------
    // UPDATE Theater
    // --------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<TheaterDTO> updateTheater(@PathVariable Long id, @RequestBody TheaterDTO dto) {
        return theaterService.getTheaterById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setAddress(dto.getAddress());
                    existing.setCity(dto.getCity());
                    existing.setState(dto.getState());
                    existing.setPostalCode(dto.getPostalCode());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    Theater updated = theaterService.saveTheater(existing);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // --------------------------------------
    // DELETE Theater
    // --------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        return theaterService.getTheaterById(id)
                .map(th -> {
                    theaterService.deleteTheater(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================
    private TheaterDTO toDTO(Theater entity) {
        TheaterDTO dto = new TheaterDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPostalCode(entity.getPostalCode());
        dto.setPhoneNumber(entity.getPhoneNumber());
        return dto;
    }

    private Theater toEntity(TheaterDTO dto) {
        Theater theater = new Theater();
        theater.setName(dto.getName());
        theater.setAddress(dto.getAddress());
        theater.setCity(dto.getCity());
        theater.setState(dto.getState());
        theater.setPostalCode(dto.getPostalCode());
        theater.setPhoneNumber(dto.getPhoneNumber());
        return theater;
    }
}

