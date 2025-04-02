package com.keyin.server.controller;

import com.keyin.server.dto.MovieDTO;
import com.keyin.server.model.Movie;
import com.keyin.server.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        List<MovieDTO> dtos = movies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(movie -> ResponseEntity.ok(toDTO(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDTO>> getMoviesByGenre(@PathVariable String genre) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        List<MovieDTO> dtos = movies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(@RequestParam String title) {
        List<Movie> movies = movieService.searchMovies(title);
        List<MovieDTO> dtos = movies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<MovieDTO>> getUpcomingMovies() {
        List<Movie> upcoming = movieService.getUpcomingMovies();
        List<MovieDTO> dtos = upcoming.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO dto) {
        Movie movieEntity = toEntity(dto);
        Movie saved = movieService.saveMovie(movieEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(m -> {
                    movieService.deleteMovie(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

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
}


