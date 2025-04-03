package com.keyin.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keyin.server.dto.MovieDTO;
import com.keyin.server.dto.ShowTimeDTO;
import com.keyin.server.model.Movie;
import com.keyin.server.model.ShowTime;
import com.keyin.server.service.MovieService;
import com.keyin.server.service.ShowTimeService;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieService movieService;
    private final ShowTimeService showTimeService;

    @Autowired
    public MovieController(MovieService movieService, ShowTimeService showTimeService) {
        this.movieService = movieService;
        this.showTimeService = showTimeService;
    }

    // ----------------------------------------------------------------
    // GET /api/movies -> list all
    // ----------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        List<MovieDTO> dtos = movies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ----------------------------------------------------------------
    // GET /api/movies/{id} -> get single movie
    // ----------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(movie -> ResponseEntity.ok(toDTO(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ----------------------------------------------------------------
    // GET /api/movies/{movieId}/showtimes
    // This is the nested route your frontend calls:
    // showtimeService.getShowtimesByMovie(movieId) => GET /api/movies/{movieId}/showtimes
    // ----------------------------------------------------------------
    @GetMapping("/{movieId}/showtimes")
    public ResponseEntity<List<ShowTimeDTO>> getShowTimesByMovie(@PathVariable Long movieId) {
        // 1) Grab showtimes from the DB
        List<ShowTime> showTimes = showTimeService.getShowTimesByMovie(movieId);
        // 2) Convert each ShowTime to ShowTimeDTO
        List<ShowTimeDTO> dtos = showTimes.stream()
                .map(this::toShowTimeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Example: GET /api/movies/genre/{genre}
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDTO>> getMoviesByGenre(@PathVariable String genre) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        List<MovieDTO> dtos = movies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Example: GET /api/movies/search?title=...
    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(@RequestParam String title) {
        List<Movie> movies = movieService.searchMovies(title);
        List<MovieDTO> dtos = movies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Example: GET /api/movies/upcoming
    @GetMapping("/upcoming")
    public ResponseEntity<List<MovieDTO>> getUpcomingMovies() {
        List<Movie> upcoming = movieService.getUpcomingMovies();
        List<MovieDTO> dtos = upcoming.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ----------------------------------------------------------------
    // POST /api/movies -> create a new movie
    // ----------------------------------------------------------------
    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO dto) {
        Movie movieEntity = toEntity(dto);
        Movie saved = movieService.saveMovie(movieEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // ----------------------------------------------------------------
    // PUT /api/movies/{id} -> update existing
    // ----------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @RequestBody MovieDTO dto) {
        return movieService.getMovieById(id)
                .map(existing -> {
                    existing.setTitle(dto.getTitle());
                    existing.setDescription(dto.getDescription());
                    existing.setDurationMinutes(dto.getDurationMinutes());
                    existing.setReleaseDate(dto.getReleaseDate());
                    existing.setGenre(dto.getGenre());
                    existing.setRating(dto.getRating());
                    existing.setPosterImageUrl(dto.getPosterImageUrl());
                    existing.setTrailerUrl(dto.getTrailerUrl());

                    Movie updated = movieService.saveMovie(existing);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ----------------------------------------------------------------
    // DELETE /api/movies/{id} -> remove
    // ----------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(m -> {
                    movieService.deleteMovie(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================
    // Private helpers
    // ============================================
    private MovieDTO toDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDurationMinutes(movie.getDurationMinutes());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setGenre(movie.getGenre());
        dto.setRating(movie.getRating());
        dto.setPosterImageUrl(movie.getPosterImageUrl());
        dto.setTrailerUrl(movie.getTrailerUrl());
        return dto;
    }

    private Movie toEntity(MovieDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDurationMinutes(dto.getDurationMinutes());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setGenre(dto.getGenre());
        movie.setRating(dto.getRating());
        movie.setPosterImageUrl(dto.getPosterImageUrl());
        movie.setTrailerUrl(dto.getTrailerUrl());
        return movie;
    }

    // Convert from ShowTime -> ShowTimeDTO for the /{movieId}/showtimes endpoint
    private ShowTimeDTO toShowTimeDTO(ShowTime showTime) {
        ShowTimeDTO dto = new ShowTimeDTO();
        dto.setId(showTime.getId());
        dto.setStartTime(showTime.getStartTime());
        dto.setEndTime(showTime.getEndTime());
        dto.setDate(showTime.getDate());
        dto.setPrice(showTime.getPrice());

        // Link movie
        if (showTime.getMovie() != null) {
            dto.setMovieId(showTime.getMovie().getId());
            dto.setMovieTitle(showTime.getMovie().getTitle());
        }

        // Link screen + theater name
        if (showTime.getScreen() != null) {
            dto.setScreenId(showTime.getScreen().getId());
            if (showTime.getScreen().getTheater() != null) {
                dto.setTheaterName(showTime.getScreen().getTheater().getName());
            }
        }
        return dto;
    }
}
