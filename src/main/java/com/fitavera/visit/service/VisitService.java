package com.fitavera.visit.service;

import com.fitavera.location.repository.entity.Location;
import com.fitavera.location.service.LocationService;
import com.fitavera.payment.service.ClientPaymentService;
import com.fitavera.schedule.repository.entity.Schedule;
import com.fitavera.schedule.service.ScheduleService;
import com.fitavera.user.repository.entity.User;
import com.fitavera.user.service.client.ClientService;
import com.fitavera.visit.repository.VisitRepository;
import com.fitavera.visit.repository.dto.VisitCountView;
import com.fitavera.visit.repository.entity.Visit;
import com.fitavera.visit.rest.model.CreateVisitRequest;
import com.fitavera.visit.service.dto.ScheduleRegistrationSummaryView;
import com.fitavera.visit.service.exception.VisitCancellationException;
import com.fitavera.visit.service.exception.VisitNotFoundException;
import com.fitavera.visit.service.exception.VisitRegistrationException;
import com.fitavera.visit.service.mapper.ScheduleRegistrationSummaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {

    // TODO make it configurable
    private static final int CANCEL_UNTIL_IN_HOURS = 4;

    private final ClientService clientService;
    private final ScheduleService scheduleService;
    private final VisitRepository visitRepository;
    private final ClientPaymentService clientPaymentService;
    private final LocationService locationService;
    private final ScheduleRegistrationSummaryMapper mapper;

    @Transactional
    public Visit register(UUID tenantId, UUID locationId, UUID scheduleId, CreateVisitRequest request, UUID clientId) {
        User client = clientService.findById(tenantId, clientId);
        Schedule schedule = scheduleService.findById(tenantId, locationId, scheduleId);

        if (!schedule.getDaysOfWeek().contains(request.date().getDayOfWeek())) {
            throw new VisitRegistrationException("Date of week doesn't match to schedule");
        }

        if (visitRepository.existsByClientAndScheduleAndDate(client, schedule, request.date())) {
            throw new VisitRegistrationException("Client is already registered for this training and date");
        }

        long count = visitRepository.countByScheduleAndDate(schedule, request.date());
        if (count >= schedule.getClientCapacity()) {
            throw new VisitRegistrationException("Max capacity exceeded");
        }

        clientPaymentService.countVisit(tenantId, clientId, schedule.getTraining().getId());

        Visit visit = new Visit();
        visit.setDate(request.date());
        visit.setSchedule(schedule);
        visit.setClient(client);

        visit = visitRepository.save(visit);

        return visit;
    }

    @Transactional
    public void cancel(UUID scheduleId, UUID visitId, UUID clientId) {
        Visit visit = findById(scheduleId, visitId, clientId);
        Schedule schedule = visit.getSchedule();
        LocalTime startTime = schedule.getStartTime();
        OffsetDateTime visitDateTime = visit.getDate().atTime(startTime).atZone(java.time.ZoneId.systemDefault())
                .toOffsetDateTime();
        OffsetDateTime now = OffsetDateTime.now();

        if (visitDateTime.isBefore(now)) {
            throw new VisitCancellationException("You can't cancel a visit in the past");
        }

        if (visit.getDate().isEqual(LocalDate.now()) &&
                ChronoUnit.HOURS.between(visitDateTime, now) < CANCEL_UNTIL_IN_HOURS) {
            throw new VisitCancellationException("You can cancel before " + CANCEL_UNTIL_IN_HOURS + " hours till training.");
        }
        visitRepository.delete(visit);
    }

    @Transactional(readOnly = true)
    public Visit findById(UUID scheduleId, UUID visitId, UUID clientId) {
        Visit visit = visitRepository.findById(visitId).orElseThrow(() -> new VisitNotFoundException(visitId));
        if (!visit.getClient().getId().equals(clientId) || !visit.getSchedule().getId().equals(scheduleId)) {
            throw new VisitRegistrationException("Client id mismatch");
        }
        return visit;
    }

    @Transactional(readOnly = true)
    public Page<Visit> findAll(UUID tenantId, UUID locationId, UUID clientId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {
        User client = clientService.findById(tenantId, clientId);
        Location location = locationService.findById(tenantId, locationId);
        return visitRepository.findAllByClientAndLocationAndDateBetween(client, location, dateFrom, dateTo, pageable);
    }

    @Transactional(readOnly = true)
    public List<ScheduleRegistrationSummaryView> getSchedulesView(UUID tenantId, UUID locationId, LocalDate dateFrom, LocalDate dateTo) {
        List<Schedule> schedules = scheduleService.findByLocation(tenantId, locationId);
        List<VisitCountView> visitCounts = visitRepository.findVisitCountsByLocationAndDateRange(locationId, dateFrom, dateTo);

        Map<UUID, List<VisitCountView>> visitCountsBySchedule = visitCounts.stream()
                .collect(Collectors.groupingBy(VisitCountView::scheduleId));

        return schedules.stream()
                .map(schedule -> mapper.toSummaryView(
                        schedule,
                        visitCountsBySchedule.getOrDefault(schedule.getId(), List.of())
                )).toList();
    }
}
