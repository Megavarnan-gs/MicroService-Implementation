package com.regulareedge.compliancecoreservice.service;

import com.regulareedge.compliancecoreservice.common.enums.CalendarStatus;
import com.regulareedge.compliancecoreservice.dto.request.ComplianceCalendarRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateCalendarStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.ComplianceCalendarResponse;
import com.regulareedge.compliancecoreservice.entity.ComplianceCalendar;
import com.regulareedge.compliancecoreservice.entity.RegulatoryObligation;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.repository.ComplianceCalendarRepository;
import com.regulareedge.compliancecoreservice.repository.RegulatoryObligationRepository;
import com.regulareedge.compliancecoreservice.service.implementation.CalendarServiceImpl;
import com.regulareedge.compliancecoreservice.service.interfaces.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarServiceImplTest {

    @Mock
    private ComplianceCalendarRepository calendarRepository;

    @Mock
    private RegulatoryObligationRepository obligationRepository;

    @Mock
    private AuditLogService auditLogService;

    private CalendarServiceImpl calendarService;

    private RegulatoryObligation obligation;
    private ComplianceCalendar calendar;

    @BeforeEach
    void setUp() {
        calendarService = new CalendarServiceImpl(calendarRepository, obligationRepository, auditLogService);

        obligation = new RegulatoryObligation();
        obligation.setObligationId(10);

        calendar = new ComplianceCalendar();
        calendar.setCalendarId(100);
        calendar.setObligation(obligation);
        calendar.setDueDate(LocalDate.now().plusDays(5));
        calendar.setDataCutOffDate(LocalDate.now().plusDays(3));
        calendar.setReminderDays(2);
        calendar.setStatus(CalendarStatus.PENDING);
    }

    @Test
    void create_shouldPersistCalendarEntry_whenObligationExists() {
        ComplianceCalendarRequest request = new ComplianceCalendarRequest();
        request.setObligationId(10);
        request.setDueDate(LocalDate.now().plusDays(5));
        request.setDataCutOffDate(LocalDate.now().plusDays(3));
        request.setReminderDays(2);

        when(obligationRepository.findById(10)).thenReturn(Optional.of(obligation));
        when(calendarRepository.save(any(ComplianceCalendar.class))).thenReturn(calendar);

        ComplianceCalendarResponse response = calendarService.create(request);

        assertEquals(10, response.getObligationId());
        assertEquals(CalendarStatus.PENDING, response.getStatus());
    }

    @Test
    void create_shouldThrow_whenObligationMissing() {
        ComplianceCalendarRequest request = new ComplianceCalendarRequest();
        request.setObligationId(999);
        request.setDueDate(LocalDate.now().plusDays(5));
        request.setDataCutOffDate(LocalDate.now().plusDays(3));

        when(obligationRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> calendarService.create(request));
    }

    @Test
    void getByObligation_shouldReturnMatchingEntries() {
        when(calendarRepository.findByObligation_ObligationId(10)).thenReturn(List.of(calendar));

        List<ComplianceCalendarResponse> responses = calendarService.getByObligation(10);

        assertEquals(1, responses.size());
        assertEquals(100, responses.get(0).getCalendarId());
    }

    @Test
    void updateStatus_shouldUpdateAndReturnEntry_whenExists() {
        when(calendarRepository.findById(100)).thenReturn(Optional.of(calendar));
        when(calendarRepository.save(any(ComplianceCalendar.class))).thenReturn(calendar);

        UpdateCalendarStatusRequest request = new UpdateCalendarStatusRequest();
        request.setCalendarId(100);
        request.setStatus(CalendarStatus.COMPLETED);

        ComplianceCalendarResponse response = calendarService.updateStatus(request);

        assertEquals(100, response.getCalendarId());
    }

    @Test
    void updateStatus_shouldThrow_whenEntryMissing() {
        when(calendarRepository.findById(404)).thenReturn(Optional.empty());

        UpdateCalendarStatusRequest request = new UpdateCalendarStatusRequest();
        request.setCalendarId(404);
        request.setStatus(CalendarStatus.COMPLETED);

        assertThrows(ResourceNotFoundException.class, () -> calendarService.updateStatus(request));
    }
}
