package com.keyin.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keyin.server.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Additional query methods if needed
}
