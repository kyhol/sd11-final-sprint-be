package com.keyin.server.repository;

import com.keyin.server.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenre(String genre);
    List<Movie> findByReleaseDateAfter(LocalDate date);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}