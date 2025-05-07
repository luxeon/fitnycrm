package com.fitnycrm.visit.repository;

import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.schedule.repository.entity.Schedule;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.visit.repository.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {

    long countByScheduleAndDate(Schedule schedule, LocalDate date);

    boolean existsByClientAndScheduleAndDate(User client, Schedule schedule, LocalDate date);

    @Query(value = "FROM Visit v JOIN v.schedule s WHERE v.client = :client AND s.location = :location " +
            "AND (CAST(:dateFrom AS java.time.LocalDate) IS NULL OR v.date >= :dateFrom) " +
            "AND (CAST(:dateTo AS java.time.LocalDate) IS NULL OR v.date <= :dateTo)")
    Page<Visit> findAllByClientAndLocationAndDateBetween(User client, Location location,
                                                         @Param("dateFrom") LocalDate dateFrom,
                                                         @Param("dateTo") LocalDate dateTo,
                                                         Pageable pageable);

}
