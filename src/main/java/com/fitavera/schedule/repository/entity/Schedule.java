package com.fitavera.schedule.repository.entity;

import com.fitavera.location.repository.entity.Location;
import com.fitavera.training.repository.entity.Training;
import com.fitavera.user.repository.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.SortedSet;
import java.util.UUID;

@Data
@Entity
@Table(name = "schedules")
@ToString(exclude = {"training", "location", "defaultTrainer"})
@EqualsAndHashCode(exclude = {"training", "location", "defaultTrainer"})
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Enumerated(EnumType.STRING)
    @Column(name = "days_of_week", columnDefinition = "day_of_week[]")
    private SortedSet<DayOfWeek> daysOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_trainer_id", nullable = false)
    private User defaultTrainer;

    @Min(1)
    @Column(nullable = false)
    private Integer clientCapacity;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
} 