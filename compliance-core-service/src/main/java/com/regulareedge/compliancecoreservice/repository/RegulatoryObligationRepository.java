package com.regulareedge.compliancecoreservice.repository;

import com.regulareedge.compliancecoreservice.common.enums.ObligationStatus;
import com.regulareedge.compliancecoreservice.entity.RegulatoryObligation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegulatoryObligationRepository extends JpaRepository<RegulatoryObligation, Integer> {

    List<RegulatoryObligation> findByStatus(ObligationStatus status);

    List<RegulatoryObligation> findByRegulator_RegulatorId(int regulatorId);

    List<RegulatoryObligation> findByOwnerId(int ownerId);
}
