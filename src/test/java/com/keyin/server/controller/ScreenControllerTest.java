package com.keyin.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyin.server.dto.ScreenDTO;
import com.keyin.server.model.Screen;
import com.keyin.server.model.Theater;
import com.keyin.server.service.ScreenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(ScreenController.class)
class ScreenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreenService screenService;

    @Autowired
    private ObjectMapper objectMapper;

    private Theater theater1;
    private Screen screen1;
    private Screen screen2;
    private ScreenDTO screenDTO1;

    @BeforeEach
    void setUp() {
        theater1 = new Theater("Cineplex Avalon");
        theater1.setId(1L);

        screen1 = new Screen(theater1, 1, 100, "Standard");
        screen1.setId(10L);

        screen2 = new Screen(theater1, 2, 150, "IMAX");
        screen2.setId(11L);

        screenDTO1 = new ScreenDTO();
        screenDTO1.setId(10L);
        screenDTO1.setTheaterId(1L);
        screenDTO1.setScreenNumber(1);
        screenDTO1.setCapacity(100);
        screenDTO1.setScreenType("Standard");
    }

    @Test
    void getAllScreens_shouldReturnListOfScreens() throws Exception {
        given(screenService.getAllScreens()).willReturn(Arrays.asList(screen1, screen2));

        mockMvc.perform(get("/api/screens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(10)))
                .andExpect(jsonPath("$[1].id", is(11)));

        verify(screenService, times(1)).getAllScreens();
    }

    @Test
    void getScreenById_whenExists_shouldReturnScreen() throws Exception {
        given(screenService.getScreenById(eq(10L))).willReturn(Optional.of(screen1));

        mockMvc.perform(get("/api/screens/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.screenNumber", is(1)));

        verify(screenService, times(1)).getScreenById(eq(10L));
    }

    @Test
    void createScreen_shouldReturnCreatedScreen() throws Exception {
        ScreenDTO requestDTO = new ScreenDTO();
        requestDTO.setTheaterId(1L);
        requestDTO.setScreenNumber(3);
        requestDTO.setCapacity(80);
        requestDTO.setScreenType("VIP");

        Screen savedScreen = new Screen(theater1, 3, 80, "VIP");
        savedScreen.setId(12L);

        given(screenService.saveScreen(any(Screen.class))).willReturn(savedScreen);

        mockMvc.perform(post("/api/screens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(12)))
                .andExpect(jsonPath("$.screenNumber", is(3)))
                .andExpect(jsonPath("$.theaterId", is(1)));

        verify(screenService, times(1)).saveScreen(any(Screen.class));
    }
}