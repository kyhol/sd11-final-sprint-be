package com.keyin.server.service;

import com.keyin.server.model.Theater;
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
class TheaterServiceTest {

    @Mock
    private TheaterRepository theaterRepository;

    @InjectMocks
    private TheaterService theaterService;

    private Theater theater1;
    private Theater theater2;

    @BeforeEach
    void setUp() {
        theater1 = new Theater("Cineplex Avalon", "123 Kenmount Rd", "St. John's", "NL", "A1B 1A1");
        theater1.setId(1L);
        theater2 = new Theater("Scotiabank Theatre", "48 Kenmount Rd", "St. John's", "NL", "A1B 1B1");
        theater2.setId(2L);
    }

    @Test
    void getAllTheaters_shouldReturnAllTheaters() {
        given(theaterRepository.findAll()).willReturn(Arrays.asList(theater1, theater2));
        List<Theater> theaters = theaterService.getAllTheaters();
        assertThat(theaters).hasSize(2).containsExactly(theater1, theater2);
        verify(theaterRepository, times(1)).findAll();
    }

    @Test
    void getTheaterById_whenExists_shouldReturnTheater() {
        given(theaterRepository.findById(1L)).willReturn(Optional.of(theater1));
        Optional<Theater> theater = theaterService.getTheaterById(1L);
        assertThat(theater).isPresent().contains(theater1);
        verify(theaterRepository, times(1)).findById(1L);
    }

    @Test
    void saveTheater_shouldReturnSavedTheater() {
        Theater newTheater = new Theater("New Downtown Cinema");
        Theater savedTheater = new Theater("New Downtown Cinema");
        savedTheater.setId(3L);

        given(theaterRepository.save(any(Theater.class))).willReturn(savedTheater);
        Theater result = theaterService.saveTheater(newTheater);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("New Downtown Cinema");
        verify(theaterRepository, times(1)).save(newTheater);
    }
}