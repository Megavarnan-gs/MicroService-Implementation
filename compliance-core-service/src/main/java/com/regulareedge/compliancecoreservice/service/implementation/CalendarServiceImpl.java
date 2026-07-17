package com.regulareedge.compliancecoreservice.service.implementation;

import com.regulareedge.compliancecoreservice.common.enums.CalendarStatus;
import com.regulareedge.compliancecoreservice.dto.request.ComplianceCalendarRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateCalendarStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.ComplianceCalendarResponse;
import com.regulareedge.compliancecoreservice.entity.ComplianceCalendar;
import com.regulareedge.compliancecoreservice.entity.RegulatoryObligation;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.mapper.CalendarMapper;
import com.regulareedge.compliancecoreservice.repository.ComplianceCalendarRepository;
import com.regulareedge.compliancecoreservice.repository.RegulatoryObligationRepository;
import com.regulareedge.compliancecoreservice.service.interfaces.AuditLogService;
import com.regulareedge.compliancecoreservice.service.interfaces.CalendarService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalendarServiceImpl implements CalendarService {

    private final ComplianceCalendarRepository calendarRepository;
    private final RegulatoryObligationRepository obligationRepository;
    private final AuditLogService auditLogService;

    public CalendarServiceImpl(ComplianceCalendarRepository calendarRepository,
                                RegulatoryObligationRepository obligationRepository,
                                AuditLogService auditLogService) {
        this.calendarRepository = calendarRepository;
        this.obligationRepository = obligationRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public ComplianceCalendarResponse create(ComplianceCalendarRequest request) {
        RegulatoryObligation obligation = obligationRepository.findById(request.getObligationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Obligation not found with id: " + request.getObligationId()));

        ComplianceCalendar calendar = new ComplianceCalendar();
        calendar.setObligation(obligation);
        calendar.setDueDate(request.getDueDate());
        calendar.setDataCutOffDate(request.getDataCutOffDate());
        calendar.setReminderDays(request.getReminderDays());
        calendar.setStatus(CalendarStatus.PENDING);

        ComplianceCalendar saved = calendarRepository.save(calendar);
        auditLogService.log(null, "CALENDAR_CREATED", "ComplianceCalendar", saved.getCalendarId());
        return CalendarMapper.toResponse(saved);
    }

    @Override
    public List<ComplianceCalendarResponse> getAll() {
        return calendarRepository.findAll().stream()
                .map(CalendarMapper::toResponse)
                .toList();
    }

    @Override
    public List<ComplianceCalendarResponse> getUpcoming() {
        LocalDate today = LocalDate.now();
        return calendarRepository.findByDueDateBetween(today, today.plusDays(30)).stream()
                .map(CalendarMapper::toResponse)
                .toList();
    }

    @Override
    public List<ComplianceCalendarResponse> getOverdue() {
        return calendarRepository.findByDueDateBefore(LocalDate.now()).stream()
                .map(CalendarMapper::toResponse)
                .toList();
    }

    @Override
    public List<ComplianceCalendarResponse> getByObligation(int obligationId) {
        return calendarRepository.findByObligation_ObligationId(obligationId).stream()
                .map(CalendarMapper::toResponse)
                .toList();
    }

    @Override
    public ComplianceCalendarResponse updateStatus(UpdateCalendarStatusRequest request) {
        ComplianceCalendar calendar = calendarRepository.findById(request.getCalendarId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calendar entry not found with id: " + request.getCalendarId()));

        calendar.setStatus(request.getStatus());
        ComplianceCalendar saved = calendarRepository.save(calendar);
        auditLogService.log(null, "CALENDAR_STATUS_UPDATED", "ComplianceCalendar", saved.getCalendarId());
        return CalendarMapper.toResponse(saved);
    }
}
