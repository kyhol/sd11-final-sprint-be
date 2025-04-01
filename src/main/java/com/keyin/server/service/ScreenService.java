package com.keyin.server.service;

import com.keyin.server.model.Screen;
import com.keyin.server.model.Theater;
import com.keyin.server.repository.ScreenRepository;
import com.keyin.server.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;

    @Autowired
    public ScreenService(ScreenRepository screenRepository, TheaterRepository theaterRepository) {
        this.screenRepository = screenRepository;
        this.theaterRepository = theaterRepository;
    }

    public List<Screen> getAllScreens() {
        return screenRepository.findAll();
    }

    public Optional<Screen> getScreenById(Long id) {
        return screenRepository.findById(id);
    }

    public List<Screen> getScreensByTheater(Long theaterId) {
        Optional<Theater> theater = theaterRepository.findById(theaterId);
        return theater.map(screenRepository::findByTheater).orElse(List.of());
    }

    public List<Screen> getScreensByType(String screenType) {
        return screenRepository.findByScreenType(screenType);
    }

    public Screen saveScreen(Screen screen) {
        return screenRepository.save(screen);
    }

    public Optional<Screen> updateScreen(Long id, Screen screenDetails) {
        return screenRepository.findById(id)
                .map(existingScreen -> {
                    if (screenDetails.getScreenNumber() != null) {
                        existingScreen.setScreenNumber(screenDetails.getScreenNumber());
                    }
                    if (screenDetails.getCapacity() != null) {
                        existingScreen.setCapacity(screenDetails.getCapacity());
                    }
                    if (screenDetails.getScreenType() != null) {
                        existingScreen.setScreenType(screenDetails.getScreenType());
                    }
                    if (screenDetails.getTheater() != null) {
                        existingScreen.setTheater(screenDetails.getTheater());
                    }
                    return screenRepository.save(existingScreen);
                });
    }

    public void deleteScreen(Long id) {
        screenRepository.deleteById(id);
    }
}