package com.keyin.server.repository;

import com.keyin.server.model.Screen;
import com.keyin.server.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByTheater(Theater theater);
    List<Screen> findByScreenType(String screenType);
}