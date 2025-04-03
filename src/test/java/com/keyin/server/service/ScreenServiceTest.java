package com.keyin.server.service;

import com.keyin.server.model.Screen;
import com.keyin.server.model.Theater;
import com.keyin.server.repository.ScreenRepository;
import com.keyin.server.repository.TheaterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreenServiceTest {

    @Mock
    private ScreenRepository screenRepository;

    @Mock
    private TheaterRepository theaterRepository;

    @InjectMocks
    private ScreenService screenService;

    private Theater theater1;
    private Screen screen1;
    private Screen screen2;

    @BeforeEach
    void setUp() {
        theater1 = new Theater("Cineplex Avalon");
        theater1.setId(1L);

        screen1 = new Screen(theater1, 1, 100, "Standard");
        screen1.setId(10L);

        screen2 = new Screen(theater1, 2, 150, "IMAX");
        screen2.setId(11L);
    }

    @Test
    void getAllScreens_shouldReturnAllScreens() {
        given(screenRepository.findAll()).willReturn(Arrays.asList(screen1, screen2));
        List<Screen> screens = screenService.getAllScreens();
        assertThat(screens).hasSize(2).containsExactly(screen1, screen2);
        verify(screenRepository, times(1)).findAll();
    }

    @Test
    void getScreenById_whenExists_shouldReturnScreen() {
        given(screenRepository.findById(10L)).willReturn(Optional.of(screen1));
        Optional<Screen> foundScreen = screenService.getScreenById(10L);
        assertThat(foundScreen).isPresent().contains(screen1);
        verify(screenRepository, times(1)).findById(10L);
    }

    @Test
    void getScreensByTheater_whenTheaterExists_shouldReturnScreens() {
        Long theaterId = 1L;
        given(theaterRepository.findById(theaterId)).willReturn(Optional.of(theater1));
        given(screenRepository.findByTheater(theater1)).willReturn(Arrays.asList(screen1, screen2));
        List<Screen> screens = screenService.getScreensByTheater(theaterId);
        assertThat(screens).hasSize(2).containsExactly(screen1, screen2);
        verify(theaterRepository, times(1)).findById(theaterId);
        verify(screenRepository, times(1)).findByTheater(theater1);
    }

    @Test
    void getScreensByTheater_whenTheaterNotExists_shouldReturnEmptyList() {
        Long nonExistentTheaterId = 99L;
        given(theaterRepository.findById(nonExistentTheaterId)).willReturn(Optional.empty());
        List<Screen> screens = screenService.getScreensByTheater(nonExistentTheaterId);
        assertThat(screens).isNotNull().isEmpty();
        verify(theaterRepository, times(1)).findById(nonExistentTheaterId);
        verify(screenRepository, never()).findByTheater(any(Theater.class));
    }

    @Test
    void getScreensByTheater_whenTheaterExistsButHasNoScreens_shouldReturnEmptyList() {
        Long theaterIdWithNoScreens = 2L;
        Theater theaterWithoutScreens = new Theater("Empty Cineplex");
        theaterWithoutScreens.setId(theaterIdWithNoScreens);
        given(theaterRepository.findById(theaterIdWithNoScreens)).willReturn(Optional.of(theaterWithoutScreens));
        given(screenRepository.findByTheater(theaterWithoutScreens)).willReturn(List.of());
        List<Screen> screens = screenService.getScreensByTheater(theaterIdWithNoScreens);
        assertThat(screens).isNotNull().isEmpty();
        verify(theaterRepository, times(1)).findById(theaterIdWithNoScreens);
        verify(screenRepository, times(1)).findByTheater(theaterWithoutScreens);
    }

    @Test
    void saveScreen_shouldReturnSavedScreen() {
        Screen newScreen = new Screen(theater1, 3, 90, "VIP");
        Screen savedScreen = new Screen(theater1, 3, 90, "VIP");
        savedScreen.setId(12L);
        given(screenRepository.save(any(Screen.class))).willReturn(savedScreen);
        Screen result = screenService.saveScreen(newScreen);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(12L);
        assertThat(result.getScreenNumber()).isEqualTo(3);
        verify(screenRepository, times(1)).save(newScreen);
    }
}