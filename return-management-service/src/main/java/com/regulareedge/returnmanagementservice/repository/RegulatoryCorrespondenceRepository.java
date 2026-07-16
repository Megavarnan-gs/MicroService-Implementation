package com.regulareedge.returnmanagementservice.repository;

import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceStatus;
import com.regulareedge.returnmanagementservice.entity.RegulatoryCorrespondence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegulatoryCorrespondenceRepository extends JpaRepository<RegulatoryCorrespondence, Integer> {

    List<RegulatoryCorrespondence> findByRegulatorId(int regulatorId);

    List<RegulatoryCorrespondence> findByStatus(CorrespondenceStatus status);
}
