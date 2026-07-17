package com.regulareedge.compliancecoreservice.service.implementation;

import com.regulareedge.compliancecoreservice.dto.response.AuditLogResponse;
import com.regulareedge.compliancecoreservice.entity.AuditLog;
import com.regulareedge.compliancecoreservice.mapper.AuditLogMapper;
import com.regulareedge.compliancecoreservice.repository.AuditLogRepository;
import com.regulareedge.compliancecoreservice.service.interfaces.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
            // Fail-open: audit logging must never break the calling business operation.
            // See UserServiceClientFallback for the same fail-open philosophy applied to Feign calls.
            logger.warn("Failed to write audit log entry for action={} entityType={} recordId={} userId={}: {}",
                    action, entityType, recordId, userId, ex.getMessage());
        }
    }

    @Override
    public Page<AuditLogResponse> search(Integer userId, String entityType, String action,
                                          LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Specification<AuditLog> spec = Specification.where(null);

        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userId"), userId));
        }
        if (entityType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("entityType"), entityType));
        }
        if (action != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("action"), action));
        }
        if (startDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timestamp"), startDate));
        }
        if (endDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("timestamp"), endDate));
        }

        Pageable effectivePageable = pageable;
        if (pageable.getSort().isUnsorted()) {
            effectivePageable = org.springframework.data.domain.PageRequest.of(
                    pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "timestamp"));
        }

        return auditLogRepository.findAll(spec, effectivePageable)
                .map(AuditLogMapper::toResponse);
    }
}
