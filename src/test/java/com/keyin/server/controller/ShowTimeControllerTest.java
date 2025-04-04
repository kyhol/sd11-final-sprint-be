package com.keyin.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.keyin.server.dto.ShowTimeDTO;
import com.keyin.server.model.Movie;
import com.keyin.server.model.Screen;
import com.keyin.server.model.ShowTime;
import com.keyin.server.model.Theater;
import com.keyin.server.service.ShowTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(ShowTimeController.class)
class ShowTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShowTimeService showTimeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie movie1;
    private Theater theater1;
    private Screen screen1;
    private ShowTime showTime1;
    private ShowTime showTime2;
    private ShowTimeDTO showTimeDTO1;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        movie1 = new Movie("Inception");
        movie1.setId(1L);
        movie1.setDurationMinutes(148);

        theater1 = new Theater("Test Theater");
        theater1.setId(1L);
        screen1 = new Screen(theater1, 1);
        screen1.setId(10L);

        LocalDateTime startTime1 = LocalDateTime.of(2024, 7, 25, 19, 0);
        showTime1 = new ShowTime(movie1, screen1, startTime1, startTime1.plusMinutes(148), new BigDecimal("12.50"));
        showTime1.setId(50L);


        LocalDateTime startTime2 = LocalDateTime.of(2024, 7, 25, 22, 0);
        showTime2 = new ShowTime(movie1, screen1, startTime2, startTime2.plusMinutes(148), new BigDecimal("12.50"));
        showTime2.setId(51L);


        showTimeDTO1 = new ShowTimeDTO();
        showTimeDTO1.setId(50L);
        showTimeDTO1.setMovieId(1L);
        showTimeDTO1.setScreenId(10L);
        showTimeDTO1.setStartTime(startTime1);
        showTimeDTO1.setEndTime(startTime1.plusMinutes(148));
        showTimeDTO1.setDate(startTime1.toLocalDate());
        showTimeDTO1.setPrice(new BigDecimal("12.50"));
    }

    @Test
    void getAllShowTimes_shouldReturnListOfShowTimes() throws Exception {
        given(showTimeService.getAllShowTimes()).willReturn(Arrays.asList(showTime1, showTime2));

        mockMvc.perform(get("/api/showtimes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(50)))
                .andExpect(jsonPath("$[1].id", is(51)));

        verify(showTimeService, times(1)).getAllShowTimes();
    }

    @Test
    void getShowTimeById_whenExists_shouldReturnShowTime() throws Exception {
        given(showTimeService.getShowTimeById(eq(50L))).willReturn(Optional.of(showTime1));

        mockMvc.perform(get("/api/showtimes/{id}", 50L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(50)))
                .andExpect(jsonPath("$.movieId", is(1)))
                .andExpect(jsonPath("$.screenId", is(10)));


        verify(showTimeService, times(1)).getShowTimeById(eq(50L));
    }

    @Test
    void createShowTime_shouldReturnCreatedShowTime() throws Exception {
        ShowTimeDTO requestDTO = new ShowTimeDTO();
        requestDTO.setMovieId(1L);
        requestDTO.setScreenId(10L);
        LocalDateTime newStartTime = LocalDateTime.of(2024, 7, 26, 14, 0);
        requestDTO.setStartTime(newStartTime);
        requestDTO.setPrice(new BigDecimal("15.00"));

        ShowTime savedShowTime = new ShowTime();
        savedShowTime.setId(52L);
        savedShowTime.setMovie(movie1);
        savedShowTime.setScreen(screen1);
        savedShowTime.setStartTime(newStartTime);
        savedShowTime.calculateEndTime();
        savedShowTime.setDate(newStartTime.toLocalDate());
        savedShowTime.setPrice(new BigDecimal("15.00"));

        given(showTimeService.saveShowTime(any(ShowTime.class))).willReturn(savedShowTime);

        mockMvc.perform(post("/api/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(52)))
                .andExpect(jsonPath("$.price", is(15.00)))
                .andExpect(jsonPath("$.movieId", is(1)))
                .andExpect(jsonPath("$.screenId", is(10)));

        verify(showTimeService, times(1)).saveShowTime(any(ShowTime.class));
    }
}