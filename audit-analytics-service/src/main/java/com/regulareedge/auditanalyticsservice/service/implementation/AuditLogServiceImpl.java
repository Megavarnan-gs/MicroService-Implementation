package com.regulareedge.auditanalyticsservice.service.implementation;

import com.regulareedge.auditanalyticsservice.dto.response.AuditLogResponse;
import com.regulareedge.auditanalyticsservice.entity.AuditLog;
import com.regulareedge.auditanalyticsservice.mapper.AuditLogMapper;
import com.regulareedge.auditanalyticsservice.repository.AuditLogRepository;
import com.regulareedge.auditanalyticsservice.service.interfaces.AuditLogService;
import jakarta.persistence.criteria.Predicate;
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

/**
 * Persists audit log entries for platform-wide audit tracking. Logging is
 * fail-open by design: any failure to persist an audit entry is caught and
 * logged as a warning, never propagated, so it can never break the caller's
 * business transaction.
 */
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
        } catch (Exception e) {
            logger.warn("Failed to persist audit log entry (action={}, entityType={}, recordId={}). "
                    + "Failing open by design - caller's transaction is unaffected.", action, entityType, recordId, e);
        }
    }

    @Override
    public Page<AuditLogResponse> search(Integer userId, String entityType, String action, LocalDateTime startDate,
                                          LocalDateTime endDate, Pageable pageable) {
        Specification<AuditLog> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

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

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable effectivePageable = pageable;
        if (pageable.getSort().isUnsorted()) {
            effectivePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "timestamp"));
        }

        return auditLogRepository.findAll(specification, effectivePageable)
                .map(AuditLogMapper::toResponse);
    }
}
