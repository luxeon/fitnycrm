package com.fitnycrm.schedule.repository.entity;

import com.fitnycrm.location.repository.entity.Location;
import com.fitnycrm.training.repository.entity.Training;
import com.fitnycrm.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "schedule")
@ToString(exclude = {"training", "location", "defaultInstructor"})
@EqualsAndHashCode(exclude = {"training", "location", "defaultInstructor"})
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

    @Column(name = "days_of_week", nullable = false)
    @ElementCollection
    private Set<String> daysOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_instructor_id", nullable = false)
    private User defaultInstructor;
} 