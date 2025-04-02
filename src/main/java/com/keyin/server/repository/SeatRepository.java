package com.keyin.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keyin.server.model.Screen;
import com.keyin.server.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreen(Screen screen);
    List<Seat> findByScreenAndAvailable(Screen screen, Boolean available);
}
