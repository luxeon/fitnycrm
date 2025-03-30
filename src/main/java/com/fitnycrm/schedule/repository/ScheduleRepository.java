package com.fitnycrm.schedule.repository;

import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.schedule.repository.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    List<Schedule> findAllByLocation(Location location);
}