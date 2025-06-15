package com.fitavera.visit.repository;

import com.fitavera.location.repository.entity.Location;
import com.fitavera.schedule.repository.entity.Schedule;
import com.fitavera.user.repository.entity.User;
import com.fitavera.visit.repository.dto.VisitCountView;
import com.fitavera.visit.repository.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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

    @Query("SELECT new com.fitavera.visit.repository.dto.VisitCountView(v.schedule.id, v.date, COUNT(v.id)) " +
           "FROM Visit v " +
           "WHERE v.schedule.location.id = :locationId " +
           "AND (CAST(:dateFrom AS java.time.LocalDate) IS NULL OR v.date >= :dateFrom) " +
           "AND (CAST(:dateTo AS java.time.LocalDate) IS NULL OR v.date <= :dateTo) " +
           "GROUP BY v.schedule.id, v.date")
    List<VisitCountView> findVisitCountsByLocationAndDateRange(@Param("locationId") UUID locationId,
                                                              @Param("dateFrom") LocalDate dateFrom,
                                                              @Param("dateTo") LocalDate dateTo);

}
