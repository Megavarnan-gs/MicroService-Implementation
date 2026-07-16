package com.regulareedge.auditanalyticsservice.repository;

import com.regulareedge.auditanalyticsservice.common.enums.ReportScope;
import com.regulareedge.auditanalyticsservice.entity.ComplianceReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplianceReportRepository extends JpaRepository<ComplianceReport, Integer> {

    List<ComplianceReport> findByScope(ReportScope scope);

    Page<ComplianceReport> findByScope(ReportScope scope, Pageable pageable);
}
