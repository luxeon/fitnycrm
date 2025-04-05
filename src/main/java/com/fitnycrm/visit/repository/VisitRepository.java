package com.fitnycrm.visit.repository;

import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.visit.repository.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {

    long countByScheduleAndDate(Schedule schedule, LocalDate date);

}
