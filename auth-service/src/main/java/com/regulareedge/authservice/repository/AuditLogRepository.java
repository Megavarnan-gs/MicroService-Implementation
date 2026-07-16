package com.regulareedge.authservice.repository;

import com.regulareedge.authservice.entity.AuditLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {

    List<AuditLog> findByEmail(String email);
}
