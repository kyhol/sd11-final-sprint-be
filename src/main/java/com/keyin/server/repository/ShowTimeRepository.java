package com.keyin.server.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keyin.server.model.Movie;
import com.keyin.server.model.Screen;
import com.keyin.server.model.ShowTime;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    List<ShowTime> findByMovie(Movie movie);
    List<ShowTime> findByScreen(Screen screen);
    List<ShowTime> findByDate(LocalDate date);
    List<ShowTime> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<ShowTime> findByMovieIdAndDate(Long movieId, LocalDate date);
}