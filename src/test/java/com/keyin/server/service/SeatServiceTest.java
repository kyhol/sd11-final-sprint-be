package com.keyin.server.service;

import com.keyin.server.model.Movie;
import com.keyin.server.model.Screen;
import com.keyin.server.model.Seat;
import com.keyin.server.model.ShowTime;
import com.keyin.server.model.Theater;
import com.keyin.server.repository.ScreenRepository;
import com.keyin.server.repository.SeatRepository;
import com.keyin.server.repository.ShowTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ScreenRepository screenRepository;

    @Mock
    private ShowTimeRepository showTimeRepository;

    @InjectMocks
    private SeatService seatService;

    private Theater theater1;
    private Screen screen1;
    private Movie movie1;
    private ShowTime showTime1;
    private Seat seatA1;
    private Seat seatA2;
    private Seat seatB1;


    @BeforeEach
    void setUp() {
        theater1 = new Theater("Test Theater");
        theater1.setId(1L);
        screen1 = new Screen(theater1, 1, 100, "Standard");
        screen1.setId(10L);

        movie1 = new Movie("Test Movie");
        movie1.setId(1L);

        showTime1 = new ShowTime(movie1, screen1, LocalDateTime.now());
        showTime1.setId(50L);

        seatA1 = new Seat(screen1, "A", 1);
        seatA1.setId(100L);
        seatA1.setAvailable(true);

        seatA2 = new Seat(screen1, "A", 2);
        seatA2.setId(101L);
        seatA2.setAvailable(false);

        seatB1 = new Seat(screen1, "B", 1);
        seatB1.setId(102L);
        seatB1.setAvailable(true);
    }

    @Test
    void getSeatsByScreen_whenScreenExists_shouldReturnSeats() {
        Long screenId = 10L;
        given(screenRepository.findById(screenId)).willReturn(Optional.of(screen1));
        given(seatRepository.findByScreen(screen1)).willReturn(Arrays.asList(seatA1, seatA2, seatB1));
        List<Seat> seats = seatService.getSeatsByScreen(screenId);
        assertThat(seats).hasSize(3).containsExactlyInAnyOrder(seatA1, seatA2, seatB1);
        verify(screenRepository, times(1)).findById(screenId);
        verify(seatRepository, times(1)).findByScreen(screen1);
    }

    @Test
    void getAvailableSeatsByScreen_whenScreenExists_shouldReturnAvailableSeats() {
        Long screenId = 10L;
        given(screenRepository.findById(screenId)).willReturn(Optional.of(screen1));
        given(seatRepository.findByScreenAndAvailable(screen1, true)).willReturn(Arrays.asList(seatA1, seatB1));
        List<Seat> seats = seatService.getAvailableSeatsByScreen(screenId);
        assertThat(seats).hasSize(2).containsExactlyInAnyOrder(seatA1, seatB1);
        assertThat(seats).allMatch(Seat::getAvailable);
        verify(screenRepository, times(1)).findById(screenId);
        verify(seatRepository, times(1)).findByScreenAndAvailable(screen1, true);
    }

    @Test
    void getSeatsByShowtime_whenShowtimeExists_shouldReturnAvailableSeatsForScreen() {
        Long showtimeId = 50L;
        given(showTimeRepository.findById(showtimeId)).willReturn(Optional.of(showTime1));
        given(seatRepository.findByScreenAndAvailable(screen1, true)).willReturn(Arrays.asList(seatA1, seatB1));
        List<Seat> seats = seatService.getSeatsByShowtime(showtimeId);
        assertThat(seats).hasSize(2).containsExactlyInAnyOrder(seatA1, seatB1);
        assertThat(seats).allMatch(Seat::getAvailable);
        verify(showTimeRepository, times(1)).findById(showtimeId);
        verify(seatRepository, times(1)).findByScreenAndAvailable(eq(screen1), eq(true));
    }
    @Test
    void updateSeatAvailability_whenExists_shouldUpdateOnlyAvailabilityAndReturnUpdated() {
        // Arrange
        Long seatId = 100L;
        Boolean newAvailability = false;
        Seat existingSeat = seatA1;
        assertThat(existingSeat.getAvailable()).isTrue();

        given(seatRepository.findById(seatId)).willReturn(Optional.of(existingSeat));

        ArgumentCaptor<Seat> seatCaptor = ArgumentCaptor.forClass(Seat.class);
        given(seatRepository.save(seatCaptor.capture())).willAnswer(invocation -> invocation.getArgument(0));

        Optional<Seat> updatedSeatOpt = seatService.updateSeatAvailability(seatId, newAvailability);

        assertThat(updatedSeatOpt).isPresent();
        Seat updatedSeat = updatedSeatOpt.get();
        assertThat(updatedSeat.getId()).isEqualTo(seatId);
        assertThat(updatedSeat.getAvailable()).isEqualTo(newAvailability);
        assertThat(updatedSeat.getRowLetter()).isEqualTo(existingSeat.getRowLetter());
        assertThat(updatedSeat.getSeatNumber()).isEqualTo(existingSeat.getSeatNumber());
        assertThat(updatedSeat.getSeatType()).isEqualTo(existingSeat.getSeatType());

        verify(seatRepository, times(1)).findById(seatId);
        verify(seatRepository, times(1)).save(any(Seat.class));

        assertThat(seatCaptor.getValue().getAvailable()).isEqualTo(newAvailability);
    }
}