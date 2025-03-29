package com.fitnycrm.schedule.repository;

import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.schedule.repository.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    Page<Schedule> findAllByLocation(Location location, Pageable pageable);
}