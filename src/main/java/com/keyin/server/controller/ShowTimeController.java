package com.keyin.server.controller;

import com.keyin.server.dto.ShowTimeDTO;
import com.keyin.server.model.Movie;
import com.keyin.server.model.Screen;
import com.keyin.server.model.ShowTime;
import com.keyin.server.service.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ShowTimeDTO>> getAllShowTimes() {
        List<ShowTime> showTimes = showTimeService.getAllShowTimes();
        List<ShowTimeDTO> dtos = showTimes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowTimeDTO> getShowTimeById(@PathVariable Long id) {
        return showTimeService.getShowTimeById(id)
                .map(showTime -> ResponseEntity.ok(toDTO(showTime)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShowTimeDTO> createShowTime(@RequestBody ShowTimeDTO dto) {

        ShowTime entity = toEntity(dto);
        ShowTime saved = showTimeService.saveShowTime(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowTime(@PathVariable Long id) {
        return showTimeService.getShowTimeById(id)
                .map(st -> {
                    showTimeService.deleteShowTime(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShowTimeDTO>> getShowTimesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShowTime> showTimes = showTimeService.getShowTimesByDate(date);
        List<ShowTimeDTO> dtos = showTimes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private ShowTimeDTO toDTO(ShowTime entity) {
        ShowTimeDTO dto = new ShowTimeDTO();
        dto.setId(entity.getId());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setDate(entity.getDate());
        dto.setPrice(entity.getPrice());

        if (entity.getMovie() != null) {
            dto.setMovieId(entity.getMovie().getId());
        }
        if (entity.getScreen() != null) {
            dto.setScreenId(entity.getScreen().getId());
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

