package com.keyin.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyin.server.dto.TheaterDTO;
import com.keyin.server.model.Theater;
import com.keyin.server.service.TheaterService;
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

@WebMvcTest(TheaterController.class)
class TheaterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TheaterService theaterService;

    @Autowired
    private ObjectMapper objectMapper;

    private Theater theater1;
    private Theater theater2;
    private TheaterDTO theaterDTO1;

    @BeforeEach
    void setUp() {
        theater1 = new Theater("Cineplex Avalon", "123 Kenmount Rd", "St. John's", "NL", "A1B 1A1");
        theater1.setId(1L);
        theater1.setPhoneNumber("709-722-1234");

        theater2 = new Theater("Scotiabank Theatre", "48 Kenmount Rd", "St. John's", "NL", "A1B 1B1");
        theater2.setId(2L);
        theater2.setPhoneNumber("709-722-5678");

        theaterDTO1 = new TheaterDTO();
        theaterDTO1.setId(1L);
        theaterDTO1.setName("Cineplex Avalon");
        theaterDTO1.setAddress("123 Kenmount Rd");
        theaterDTO1.setCity("St. John's");
        theaterDTO1.setState("NL");
        theaterDTO1.setPostalCode("A1B 1A1");
        theaterDTO1.setPhoneNumber("709-722-1234");
    }

    @Test
    void getAllTheaters_shouldReturnListOfTheaters() throws Exception {
        given(theaterService.getAllTheaters()).willReturn(Arrays.asList(theater1, theater2));

        mockMvc.perform(get("/api/theaters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(theaterService, times(1)).getAllTheaters();
    }

    @Test
    void getTheaterById_whenExists_shouldReturnTheater() throws Exception {
        given(theaterService.getTheaterById(eq(1L))).willReturn(Optional.of(theater1));

        mockMvc.perform(get("/api/theaters/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Cineplex Avalon")));

        verify(theaterService, times(1)).getTheaterById(eq(1L));
    }

    @Test
    void createTheater_shouldReturnCreatedTheater() throws Exception {
        TheaterDTO requestDTO = new TheaterDTO();
        requestDTO.setName("New Theater");
        requestDTO.setCity("Mount Pearl");
        requestDTO.setState("NL");
        requestDTO.setAddress("1 First St");
        requestDTO.setPostalCode("A1N 1N1");
        requestDTO.setPhoneNumber("709-111-2222");

        Theater savedTheater = new Theater();
        savedTheater.setId(3L);
        savedTheater.setName(requestDTO.getName());
        savedTheater.setCity(requestDTO.getCity());
        savedTheater.setState(requestDTO.getState());
        savedTheater.setAddress(requestDTO.getAddress());
        savedTheater.setPostalCode(requestDTO.getPostalCode());
        savedTheater.setPhoneNumber(requestDTO.getPhoneNumber());


        given(theaterService.saveTheater(any(Theater.class))).willReturn(savedTheater);

        mockMvc.perform(post("/api/theaters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("New Theater")))
                .andExpect(jsonPath("$.city", is("Mount Pearl")));

        verify(theaterService, times(1)).saveTheater(any(Theater.class));
    }
}