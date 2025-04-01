package com.keyin.server.controller;

import com.keyin.server.model.ShowTime;
import com.keyin.server.service.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
@CrossOrigin(origins = "*")
public class ShowTimeController {

    private final ShowTimeService showTimeService;

    @Autowired
    public ShowTimeController(ShowTimeService showTimeService) {
        this.showTimeService = showTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ShowTime>> getAllShowTimes() {
        return ResponseEntity.ok(showTimeService.getAllShowTimes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowTime> getShowTimeById(@PathVariable Long id) {
        return showTimeService.getShowTimeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowTime>> getShowTimesByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showTimeService.getShowTimesByMovie(movieId));
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<ShowTime>> getShowTimesByScreen(@PathVariable Long screenId) {
        return ResponseEntity.ok(showTimeService.getShowTimesByScreen(screenId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShowTime>> getShowTimesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(showTimeService.getShowTimesByDate(date));
    }

    @GetMapping("/movie/{movieId}/date/{date}")
    public ResponseEntity<List<ShowTime>> getShowTimesByMovieAndDate(
            @PathVariable Long movieId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(showTimeService.getShowTimesByMovieAndDate(movieId, date));
    }

    @PostMapping
    public ResponseEntity<ShowTime> createShowTime(@RequestBody ShowTime showTime) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showTimeService.saveShowTime(showTime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowTime> updateShowTime(@PathVariable Long id, @RequestBody ShowTime showTime) {
        return showTimeService.getShowTimeById(id)
                .map(existingShowTime -> {
                    showTime.setId(id);
                    return ResponseEntity.ok(showTimeService.saveShowTime(showTime));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowTime(@PathVariable Long id) {
        return showTimeService.getShowTimeById(id)
                .map(showTime -> {
                    showTimeService.deleteShowTime(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}