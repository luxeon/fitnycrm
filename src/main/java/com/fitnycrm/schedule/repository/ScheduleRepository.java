package com.fitnycrm.schedule.repository;

import com.fitnycrm.schedule.repository.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    @Query("""
                FROM Schedule s JOIN s.location l WHERE l.tenant.id = :tenantId AND l.id = :locationId
            """)
    List<Schedule> findByTenantAndLocationId(UUID tenantId, UUID locationId);
}