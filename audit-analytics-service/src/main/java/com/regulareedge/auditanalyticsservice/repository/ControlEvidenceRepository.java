package com.regulareedge.auditanalyticsservice.repository;

import com.regulareedge.auditanalyticsservice.entity.ControlEvidence;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ControlEvidenceRepository extends CrudRepository<ControlEvidence, Integer> {

    List<ControlEvidence> findByReturnId(int returnId);
}
