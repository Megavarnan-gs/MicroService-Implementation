package com.regulareedge.returnmanagementservice.service.implementation;

import com.regulareedge.returnmanagementservice.common.enums.ScheduleStatus;
import com.regulareedge.returnmanagementservice.dto.request.ReturnScheduleRequest;
import com.regulareedge.returnmanagementservice.dto.response.ReturnScheduleResponse;
import com.regulareedge.returnmanagementservice.entity.RegulatoryReturn;
import com.regulareedge.returnmanagementservice.entity.ReturnSchedule;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.mapper.ReturnScheduleMapper;
import com.regulareedge.returnmanagementservice.repository.RegulatoryReturnRepository;
import com.regulareedge.returnmanagementservice.repository.ReturnScheduleRepository;
import com.regulareedge.returnmanagementservice.service.interfaces.ReturnScheduleService;
import com.regulareedge.returnmanagementservice.service.interfaces.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnScheduleServiceImpl implements ReturnScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ReturnScheduleServiceImpl.class);

    private final ReturnScheduleRepository returnScheduleRepository;
    private final RegulatoryReturnRepository regulatoryReturnRepository;
    private final UserValidationService userValidationService;

    public ReturnScheduleServiceImpl(ReturnScheduleRepository returnScheduleRepository,
                                      RegulatoryReturnRepository regulatoryReturnRepository,
                                      UserValidationService userValidationService) {
        this.returnScheduleRepository = returnScheduleRepository;
        this.regulatoryReturnRepository = regulatoryReturnRepository;
        this.userValidationService = userValidationService;
    }

    @Override
    public ReturnScheduleResponse add(ReturnScheduleRequest request) {
        RegulatoryReturn regulatoryReturn = regulatoryReturnRepository.findById(request.getReturnId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Regulatory return not found with id: " + request.getReturnId()));

        if (!userValidationService.userExists(request.getValidatedById())) {
            logger.warn("validatedById={} was reported as not found by auth-service", request.getValidatedById());
            throw new InvalidRequestException("User not found with id: " + request.getValidatedById());
        }

        ReturnSchedule schedule = new ReturnSchedule();
        schedule.setRegulatoryReturn(regulatoryReturn);
        schedule.setScheduleName(request.getScheduleName());
        schedule.setDataContent(request.getDataContent());
        schedule.setValidatedById(request.getValidatedById());
        schedule.setStatus(ScheduleStatus.DRAFT.name());

        ReturnSchedule saved = returnScheduleRepository.save(schedule);
        return ReturnScheduleMapper.toResponse(saved);
    }

    @Override
    public List<ReturnScheduleResponse> getByReturn(int returnId) {
        return returnScheduleRepository.findByRegulatoryReturn_ReturnId(returnId).stream()
                .map(ReturnScheduleMapper::toResponse)
                .toList();
    }

    @Override
    public ReturnScheduleResponse validate(int scheduleId, int validatedById) {
        ReturnSchedule schedule = returnScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Return schedule not found with id: " + scheduleId));

        schedule.setValidatedById(validatedById);
        schedule.setStatus(ScheduleStatus.VALIDATED.name());

        ReturnSchedule saved = returnScheduleRepository.save(schedule);
        return ReturnScheduleMapper.toResponse(saved);
    }
}
