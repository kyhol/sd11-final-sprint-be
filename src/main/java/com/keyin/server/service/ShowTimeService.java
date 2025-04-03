package com.keyin.server.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keyin.server.model.Movie;
import com.keyin.server.model.Screen;
import com.keyin.server.model.ShowTime;
import com.keyin.server.repository.MovieRepository;
import com.keyin.server.repository.ScreenRepository;
import com.keyin.server.repository.ShowTimeRepository;

@Service
public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

    @Autowired
    public ShowTimeService(ShowTimeRepository showTimeRepository,
                           MovieRepository movieRepository,
                           ScreenRepository screenRepository) {
        this.showTimeRepository = showTimeRepository;
        this.movieRepository = movieRepository;
        this.screenRepository = screenRepository;
    }

    public List<ShowTime> getAllShowTimes() {
        return showTimeRepository.findAll();
    }

    public Optional<ShowTime> getShowTimeById(Long id) {
        return showTimeRepository.findById(id);
    }

    public List<ShowTime> getShowTimesByMovie(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        return movie.map(showTimeRepository::findByMovie).orElse(List.of());
    }

    public List<ShowTime> getShowTimesByScreen(Long screenId) {
        Optional<Screen> screen = screenRepository.findById(screenId);
        return screen.map(showTimeRepository::findByScreen).orElse(List.of());
    }

    public List<ShowTime> getShowTimesByDate(LocalDate date) {
        return showTimeRepository.findByDate(date);
    }

    public List<ShowTime> getShowTimesByMovieAndDate(Long movieId, LocalDate date) {
        return showTimeRepository.findByMovieIdAndDate(movieId, date);
    }

    public ShowTime saveShowTime(ShowTime showTime) {
        showTime.calculateEndTime();
        return showTimeRepository.save(showTime);
    }

    public void deleteShowTime(Long id) {
        showTimeRepository.deleteById(id);
    }
}