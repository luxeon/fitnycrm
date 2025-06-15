package com.fitavera.schedule.repository;

import com.fitavera.location.repository.entity.Location;
import com.fitavera.schedule.repository.entity.Schedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    @EntityGraph(attributePaths = {"defaultTrainer", "training"})
    List<Schedule> findAllByLocation(Location location);
}