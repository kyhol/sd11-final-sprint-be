package com.keyin.server.service;

import com.keyin.server.model.Movie;
import com.keyin.server.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        movie1 = new Movie("Inception", "Mind-bending thriller", 148, LocalDate.of(2010, 7, 16), "Sci-Fi", "PG-13");
        movie1.setId(1L);
        movie2 = new Movie("The Dark Knight", "Batman battles Joker", 152, LocalDate.of(2008, 7, 18), "Action", "PG-13");
        movie2.setId(2L);
    }

    @Test
    void getAllMovies_shouldReturnAllMovies() {
        given(movieRepository.findAll()).willReturn(Arrays.asList(movie1, movie2));
        List<Movie> movies = movieService.getAllMovies();
        assertThat(movies).isNotNull();
        assertThat(movies).hasSize(2);
        assertThat(movies).containsExactly(movie1, movie2);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void getMovieById_whenMovieExists_shouldReturnMovie() {
        Long movieId = 1L;
        given(movieRepository.findById(movieId)).willReturn(Optional.of(movie1));
        Optional<Movie> foundMovie = movieService.getMovieById(movieId);
        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get()).isEqualTo(movie1);
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void saveMovie_shouldReturnSavedMovie() {
        Movie newMovie = new Movie("Interstellar");
        Movie savedMovie = new Movie("Interstellar");
        savedMovie.setId(3L);

        given(movieRepository.save(any(Movie.class))).willReturn(savedMovie);
        Movie result = movieService.saveMovie(newMovie);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getTitle()).isEqualTo("Interstellar");
        verify(movieRepository, times(1)).save(newMovie);
    }
}