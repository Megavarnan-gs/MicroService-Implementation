package com.regulareedge.compliancecoreservice.repository;

import com.regulareedge.compliancecoreservice.entity.DataQualityCheck;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataQualityCheckRepository extends CrudRepository<DataQualityCheck, Integer> {

    List<DataQualityCheck> findByRequest_RequestId(int requestId);
}
