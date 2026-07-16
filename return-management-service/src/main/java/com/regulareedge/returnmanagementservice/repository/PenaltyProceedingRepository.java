package com.regulareedge.returnmanagementservice.repository;

import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;
import com.regulareedge.returnmanagementservice.entity.PenaltyProceeding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PenaltyProceedingRepository extends JpaRepository<PenaltyProceeding, Integer> {

    List<PenaltyProceeding> findByStatus(PenaltyStatus status);
}
