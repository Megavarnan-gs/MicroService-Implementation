package com.regulareedge.notificationservice.service.implementation;

import com.regulareedge.notificationservice.dto.response.AuditLogResponse;
import com.regulareedge.notificationservice.entity.AuditLog;
import com.regulareedge.notificationservice.mapper.AuditLogMapper;
import com.regulareedge.notificationservice.repository.AuditLogRepository;
import com.regulareedge.notificationservice.service.interfaces.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void log(Integer userId, String action, String entityType, Integer recordId) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(userId);
            auditLog.setAction(action);
            auditLog.setEntityType(entityType);
            auditLog.setRecordId(recordId);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(auditLog);
        } catch (Exception ex) {
            logger.warn("Failed to persist audit log entry for action={}, entityType={}, recordId={}: {}. "
                    + "Proceeding without interrupting the caller's business operation (fail-open).",
                    action, entityType, recordId, ex.getMessage());
        }
    }

    @Override
    public Page<AuditLogResponse> search(Integer userId, String entityType, String action, LocalDateTime startDate,
                                          LocalDateTime endDate, Pageable pageable) {
        Specification<AuditLog> specification = buildSpecification(userId, entityType, action, startDate, endDate);

        Pageable effectivePageable = pageable;
        if (pageable.getSort().isUnsorted()) {
            effectivePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "timestamp"));
        }

        return auditLogRepository.findAll(specification, effectivePageable)
                .map(AuditLogMapper::toResponse);
    }

    private Specification<AuditLog> buildSpecification(Integer userId, String entityType, String action,
                                                         LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            }
            if (entityType != null) {
                predicates.add(criteriaBuilder.equal(root.get("entityType"), entityType));
            }
            if (action != null) {
                predicates.add(criteriaBuilder.equal(root.get("action"), action));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
