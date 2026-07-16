package com.regulareedge.returnmanagementservice.repository;

import com.regulareedge.returnmanagementservice.entity.RegulatoryReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegulatoryReturnRepository extends JpaRepository<RegulatoryReturn, Integer> {

    List<RegulatoryReturn> findByPreparedById(int preparedById);

    List<RegulatoryReturn> findByStatus(String status);
}
