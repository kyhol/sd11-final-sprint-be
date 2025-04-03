package com.keyin.server.controller;

import com.keyin.server.dto.ScreenDTO;
import com.keyin.server.model.Screen;
import com.keyin.server.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/screens")
@CrossOrigin(origins = "*")
public class ScreenController {

    private final ScreenService screenService;

    @Autowired
    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping
    public ResponseEntity<List<ScreenDTO>> getAllScreens() {
        List<Screen> screens = screenService.getAllScreens();
        List<ScreenDTO> dtos = screens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenDTO> getScreenById(@PathVariable Long id) {
        return screenService.getScreenById(id)
                .map(screen -> ResponseEntity.ok(toDTO(screen)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ScreenDTO>> getScreensByTheater(@PathVariable Long theaterId) {
        List<Screen> screens = screenService.getScreensByTheater(theaterId);
        List<ScreenDTO> dtos = screens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/type/{screenType}")
    public ResponseEntity<List<ScreenDTO>> getScreensByType(@PathVariable String screenType) {
        List<Screen> screens = screenService.getScreensByType(screenType);
        List<ScreenDTO> dtos = screens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ScreenDTO> createScreen(@RequestBody ScreenDTO dto) {
        Screen newScreen = toEntity(dto);
        Screen saved = screenService.saveScreen(newScreen);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScreenDTO> updateScreen(@PathVariable Long id, @RequestBody ScreenDTO dto) {
        return screenService.updateScreen(id, toEntity(dto))
                .map(updated -> ResponseEntity.ok(toDTO(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id) {
        return screenService.getScreenById(id)
                .map(scr -> {
                    screenService.deleteScreen(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private ScreenDTO toDTO(Screen screen) {
        ScreenDTO dto = new ScreenDTO();
        dto.setId(screen.getId());
        if (screen.getTheater() != null) {
            dto.setTheaterId(screen.getTheater().getId());
        }
        dto.setScreenNumber(screen.getScreenNumber());
        dto.setCapacity(screen.getCapacity());
        dto.setScreenType(screen.getScreenType());
        return dto;
    }

    private Screen toEntity(ScreenDTO dto) {
        Screen screen = new Screen();
        screen.setScreenNumber(dto.getScreenNumber());
        screen.setCapacity(dto.getCapacity());
        screen.setScreenType(dto.getScreenType());
        return screen;
    }
}
