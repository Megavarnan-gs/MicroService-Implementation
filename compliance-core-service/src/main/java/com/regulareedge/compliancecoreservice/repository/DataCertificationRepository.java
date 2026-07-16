package com.regulareedge.compliancecoreservice.repository;

import com.regulareedge.compliancecoreservice.entity.DataCertification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataCertificationRepository extends CrudRepository<DataCertification, Integer> {

    List<DataCertification> findByRequest_RequestId(int requestId);
}
