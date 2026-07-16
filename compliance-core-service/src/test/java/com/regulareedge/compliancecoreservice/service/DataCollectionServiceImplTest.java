package com.regulareedge.compliancecoreservice.service;

import com.regulareedge.compliancecoreservice.common.enums.DataRequestStatus;
import com.regulareedge.compliancecoreservice.dto.request.DataCollectionCreateRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateDataRequestStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataCollectionRequestResponse;
import com.regulareedge.compliancecoreservice.entity.ComplianceCalendar;
import com.regulareedge.compliancecoreservice.entity.DataCollectionRequest;
import com.regulareedge.compliancecoreservice.exception.InvalidRequestException;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.repository.ComplianceCalendarRepository;
import com.regulareedge.compliancecoreservice.repository.DataCollectionRequestRepository;
import com.regulareedge.compliancecoreservice.service.implementation.DataCollectionServiceImpl;
import com.regulareedge.compliancecoreservice.service.interfaces.UserValidationService;
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
class DataCollectionServiceImplTest {

    @Mock
    private DataCollectionRequestRepository requestRepository;

    @Mock
    private ComplianceCalendarRepository calendarRepository;

    @Mock
    private UserValidationService userValidationService;

    private DataCollectionServiceImpl dataCollectionService;

    private ComplianceCalendar calendar;
    private DataCollectionRequest dataRequest;

    @BeforeEach
    void setUp() {
        dataCollectionService = new DataCollectionServiceImpl(requestRepository, calendarRepository, userValidationService);

        calendar = new ComplianceCalendar();
        calendar.setCalendarId(100);

        dataRequest = new DataCollectionRequest();
        dataRequest.setRequestId(1000);
        dataRequest.setCalendar(calendar);
        dataRequest.setDataOwnerId(7);
        dataRequest.setDataDescription("Q1 liquidity data");
        dataRequest.setDataCutOffDate(LocalDate.now().plusDays(1));
        dataRequest.setSubmissionDeadline(LocalDate.now().plusDays(10));
        dataRequest.setStatus(DataRequestStatus.PENDING);
    }

    @Test
    void create_shouldPersistRequest_whenCalendarExistsAndOwnerValid() {
        DataCollectionCreateRequest request = new DataCollectionCreateRequest();
        request.setCalendarId(100);
        request.setDataOwnerId(7);
        request.setDataDescription("Q1 liquidity data");
        request.setDataCutOffDate(LocalDate.now().plusDays(1));
        request.setSubmissionDeadline(LocalDate.now().plusDays(10));

        when(calendarRepository.findById(100)).thenReturn(Optional.of(calendar));
        when(userValidationService.userExists(7)).thenReturn(true);
        when(requestRepository.save(any(DataCollectionRequest.class))).thenReturn(dataRequest);

        DataCollectionRequestResponse response = dataCollectionService.create(request);

        assertEquals(100, response.getCalendarId());
        assertEquals(DataRequestStatus.PENDING, response.getStatus());
    }

    @Test
    void create_shouldThrow_whenCalendarMissing() {
        DataCollectionCreateRequest request = new DataCollectionCreateRequest();
        request.setCalendarId(999);
        request.setDataOwnerId(7);
        request.setDataDescription("Q1 liquidity data");
        request.setDataCutOffDate(LocalDate.now().plusDays(1));
        request.setSubmissionDeadline(LocalDate.now().plusDays(10));

        when(calendarRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dataCollectionService.create(request));
    }

    @Test
    void create_shouldThrow_whenOwnerReportedMissing() {
        DataCollectionCreateRequest request = new DataCollectionCreateRequest();
        request.setCalendarId(100);
        request.setDataOwnerId(999);
        request.setDataDescription("Q1 liquidity data");
        request.setDataCutOffDate(LocalDate.now().plusDays(1));
        request.setSubmissionDeadline(LocalDate.now().plusDays(10));

        when(calendarRepository.findById(100)).thenReturn(Optional.of(calendar));
        when(userValidationService.userExists(999)).thenReturn(false);

        assertThrows(InvalidRequestException.class, () -> dataCollectionService.create(request));
    }

    @Test
    void getByDataOwner_shouldReturnMatchingRequests() {
        when(requestRepository.findByDataOwnerId(7)).thenReturn(List.of(dataRequest));

        List<DataCollectionRequestResponse> responses = dataCollectionService.getByDataOwner(7);

        assertEquals(1, responses.size());
        assertEquals(7, responses.get(0).getDataOwnerId());
    }

    @Test
    void updateStatus_shouldUpdateAndReturnRequest_whenExists() {
        when(requestRepository.findById(1000)).thenReturn(Optional.of(dataRequest));
        when(requestRepository.save(any(DataCollectionRequest.class))).thenReturn(dataRequest);

        UpdateDataRequestStatusRequest request = new UpdateDataRequestStatusRequest();
        request.setRequestId(1000);
        request.setStatus(DataRequestStatus.SUBMITTED);

        DataCollectionRequestResponse response = dataCollectionService.updateStatus(request);

        assertEquals(1000, response.getRequestId());
    }

    @Test
    void updateStatus_shouldThrow_whenRequestMissing() {
        when(requestRepository.findById(404)).thenReturn(Optional.empty());

        UpdateDataRequestStatusRequest request = new UpdateDataRequestStatusRequest();
        request.setRequestId(404);
        request.setStatus(DataRequestStatus.SUBMITTED);

        assertThrows(ResourceNotFoundException.class, () -> dataCollectionService.updateStatus(request));
    }
}
