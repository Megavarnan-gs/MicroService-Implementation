package com.regulareedge.returnmanagementservice.repository;

import com.regulareedge.returnmanagementservice.entity.SubmissionRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubmissionRecordRepository extends CrudRepository<SubmissionRecord, Integer> {

    List<SubmissionRecord> findByRegulatoryReturn_ReturnId(int returnId);
}
