package com.keyin.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyin.server.dto.MovieDTO;
import com.keyin.server.model.Movie;
import com.keyin.server.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;


@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie movie1;
    private Movie movie2;
    private MovieDTO movieDTO1;

    @BeforeEach
    void setUp() {
        movie1 = new Movie("Inception", "Mind-bending thriller", 148, LocalDate.of(2010, 7, 16), "Sci-Fi", "PG-13");
        movie1.setId(1L);
        movie1.setPosterImageUrl("url1");
        movie1.setTrailerUrl("trailer1");

        movie2 = new Movie("The Dark Knight", "Batman battles Joker", 152, LocalDate.of(2008, 7, 18), "Action", "PG-13");
        movie2.setId(2L);
        movie2.setPosterImageUrl("url2");
        movie2.setTrailerUrl("trailer2");

        movieDTO1 = new MovieDTO();
        movieDTO1.setId(1L);
        movieDTO1.setTitle(movie1.getTitle());
        movieDTO1.setDescription(movie1.getDescription());
        movieDTO1.setDurationMinutes(movie1.getDurationMinutes());
        movieDTO1.setReleaseDate(movie1.getReleaseDate());
        movieDTO1.setGenre(movie1.getGenre());
        movieDTO1.setRating(movie1.getRating());
        movieDTO1.setPosterImageUrl(movie1.getPosterImageUrl());
        movieDTO1.setTrailerUrl(movie1.getTrailerUrl());
    }

    @Test
    void getAllMovies_shouldReturnListOfMovies() throws Exception {
        given(movieService.getAllMovies()).willReturn(Arrays.asList(movie1, movie2));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Inception")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("The Dark Knight")));

        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    void getMovieById_whenMovieExists_shouldReturnMovie() throws Exception {
        given(movieService.getMovieById(eq(1L))).willReturn(Optional.of(movie1));

        mockMvc.perform(get("/api/movies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Inception")));

        verify(movieService, times(1)).getMovieById(eq(1L));
    }

    @Test
    void createMovie_shouldReturnCreatedMovie() throws Exception {
        MovieDTO requestDTO = new MovieDTO();
        requestDTO.setTitle("New Movie");
        requestDTO.setDescription("Desc");
        requestDTO.setGenre("Test");
        requestDTO.setDurationMinutes(120);
        requestDTO.setReleaseDate(LocalDate.now().plusDays(1));
        requestDTO.setRating("G");
        requestDTO.setPosterImageUrl("new_url");
        requestDTO.setTrailerUrl("new_trailer");

        given(movieService.saveMovie(any(Movie.class))).willAnswer(invocation -> {
            Movie movieToSave = invocation.getArgument(0);
            movieToSave.setId(3L);
            movieToSave.setTitle(requestDTO.getTitle());
            movieToSave.setDescription(requestDTO.getDescription());
            movieToSave.setGenre(requestDTO.getGenre());
            movieToSave.setDurationMinutes(requestDTO.getDurationMinutes());
            movieToSave.setReleaseDate(requestDTO.getReleaseDate());
            movieToSave.setRating(requestDTO.getRating());
            movieToSave.setPosterImageUrl(requestDTO.getPosterImageUrl());
            movieToSave.setTrailerUrl(requestDTO.getTrailerUrl());
            return movieToSave;
        });

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is(requestDTO.getTitle())));

        verify(movieService, times(1)).saveMovie(any(Movie.class));
    }
    @Test
    void deleteMovie_whenMovieNotExists_shouldReturnNotFound() throws Exception {
        given(movieService.getMovieById(eq(99L))).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/movies/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(movieService, times(1)).getMovieById(eq(99L));
        verify(movieService, never()).deleteMovie(anyLong());
    }
    @Test
    void getMoviesByGenre_whenNoMoviesMatch_shouldReturnEmptyList() throws Exception {
        String genreWithNoMovies = "Western";
        given(movieService.getMoviesByGenre(eq(genreWithNoMovies))).willReturn(List.of());

        mockMvc.perform(get("/api/movies/genre/{genre}", genreWithNoMovies))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(movieService, times(1)).getMoviesByGenre(eq(genreWithNoMovies));
    }
}
