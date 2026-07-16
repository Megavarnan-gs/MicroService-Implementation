package com.regulareedge.returnmanagementservice.repository;

import com.regulareedge.returnmanagementservice.entity.ReturnSchedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReturnScheduleRepository extends CrudRepository<ReturnSchedule, Integer> {

    List<ReturnSchedule> findByRegulatoryReturn_ReturnId(int returnId);
}
