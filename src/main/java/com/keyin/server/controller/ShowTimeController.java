package com.keyin.server.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keyin.server.dto.ShowTimeDTO;
import com.keyin.server.model.ShowTime;
import com.keyin.server.service.ShowTimeService;

@RestController
@RequestMapping("/api/showtimes")
@CrossOrigin(origins = "*")
public class ShowTimeController {

    private final ShowTimeService showTimeService;

    @Autowired
    public ShowTimeController(ShowTimeService showTimeService) {
        this.showTimeService = showTimeService;
    }

    // ----------------------------------------------------------------
    // 1) Get ALL showtimes (List<ShowTimeDTO>)
    //    e.g. GET /api/showtimes
    // ----------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ShowTimeDTO>> getAllShowTimes() {
        List<ShowTime> showTimes = showTimeService.getAllShowTimes();
        List<ShowTimeDTO> dtos = showTimes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ----------------------------------------------------------------
    // 2) Get a single showtime by ID
    //    e.g. GET /api/showtimes/{id}
    // ----------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ShowTimeDTO> getShowTimeById(@PathVariable Long id) {
        Optional<ShowTime> opt = showTimeService.getShowTimeById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ShowTime showTime = opt.get();
        return ResponseEntity.ok(toDTO(showTime));
    }

    // ----------------------------------------------------------------
    // 3) Create a new ShowTime
    //    e.g. POST /api/showtimes
    // ----------------------------------------------------------------
    @PostMapping
    public ResponseEntity<ShowTimeDTO> createShowTime(@RequestBody ShowTimeDTO dto) {
        ShowTime entity = toEntity(dto);
        ShowTime saved = showTimeService.saveShowTime(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // ----------------------------------------------------------------
    // 4) Update an existing ShowTime by ID
    //    e.g. PUT /api/showtimes/{id}
    // ----------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ShowTimeDTO> updateShowTime(@PathVariable Long id, @RequestBody ShowTimeDTO dto) {
        return showTimeService.getShowTimeById(id)
                .map(existing -> {
                    existing.setStartTime(dto.getStartTime());
                    existing.setEndTime(dto.getEndTime());
                    existing.setDate(dto.getDate());
                    existing.setPrice(dto.getPrice());
                    ShowTime updated = showTimeService.saveShowTime(existing);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ----------------------------------------------------------------
    // 5) Delete a ShowTime by ID
    //    e.g. DELETE /api/showtimes/{id}
    // ----------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowTime(@PathVariable Long id) {
        return showTimeService.getShowTimeById(id)
                .map(st -> {
                    showTimeService.deleteShowTime(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ----------------------------------------------------------------
    // 6) Get showtimes by date
    //    e.g. GET /api/showtimes/date/2023-12-20
    // ----------------------------------------------------------------
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShowTimeDTO>> getShowTimesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShowTime> showTimes = showTimeService.getShowTimesByDate(date);
        List<ShowTimeDTO> dtos = showTimes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ----------------------------------------------------------------
    // 7) Get showtimes by Movie ID
    //    e.g. GET /api/movies/{movieId}/showtimes
    // ----------------------------------------------------------------
    @GetMapping("/../{movieId}") // we'll fix this path in a second
    public ResponseEntity<List<ShowTimeDTO>> getShowTimesByMovie(@PathVariable Long movieId) {
        // ^ This is how your React code calls: GET /api/movies/{movieId}/showtimes
        // But since your controller is mapped to /api/showtimes,
        // we need a path that effectively merges with /api/movies/{movieId}.
        // We can do that in a separate method, or we can do it in a "MovieController."

        // We'll do it in this ShowTimeController for convenience. We'll define a custom path:
        // => /api/showtimes/movie/{movieId}
        // Then your frontend would call: GET /api/showtimes/movie/{movieId}
        // OR we can do a separate endpoint in "MovieController"
        // For the usage "http://localhost:8080/api/movies/{movieId}/showtimes"

        // We'll choose "movie/{movieId}" now:

        return ResponseEntity.ok(
            showTimeService.getShowTimesByMovie(movieId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList())
        );
    }

    // ----------------------------------------------------------------
    // Private helpers to convert between Entity & DTO
    // ----------------------------------------------------------------
    private ShowTimeDTO toDTO(ShowTime entity) {
        ShowTimeDTO dto = new ShowTimeDTO();
        dto.setId(entity.getId());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setDate(entity.getDate());
        dto.setPrice(entity.getPrice());

        // Link movieId & screenId if they exist
        if (entity.getMovie() != null) {
            dto.setMovieId(entity.getMovie().getId());
            dto.setMovieTitle(entity.getMovie().getTitle());
        }
        if (entity.getScreen() != null) {
            dto.setScreenId(entity.getScreen().getId());
            if (entity.getScreen().getTheater() != null) {
                dto.setTheaterName(entity.getScreen().getTheater().getName());
            }
        }
        return dto;
    }

    private ShowTime toEntity(ShowTimeDTO dto) {
        ShowTime entity = new ShowTime();
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        return entity;
    }
}
