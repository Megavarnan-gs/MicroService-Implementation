package com.regulareedge.compliancecoreservice.repository;

import com.regulareedge.compliancecoreservice.entity.ComplianceCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ComplianceCalendarRepository extends JpaRepository<ComplianceCalendar, Integer> {

    List<ComplianceCalendar> findByObligation_ObligationId(int obligationId);

    List<ComplianceCalendar> findByDueDateBetween(LocalDate start, LocalDate end);

    List<ComplianceCalendar> findByDueDateBefore(LocalDate date);
}
