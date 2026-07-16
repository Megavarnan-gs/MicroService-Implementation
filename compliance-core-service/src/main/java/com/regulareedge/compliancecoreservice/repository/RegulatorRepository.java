package com.regulareedge.compliancecoreservice.repository;

import com.regulareedge.compliancecoreservice.common.enums.RegulatorStatus;
import com.regulareedge.compliancecoreservice.entity.Regulator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegulatorRepository extends JpaRepository<Regulator, Integer> {

    List<Regulator> findByStatus(RegulatorStatus status);
}
