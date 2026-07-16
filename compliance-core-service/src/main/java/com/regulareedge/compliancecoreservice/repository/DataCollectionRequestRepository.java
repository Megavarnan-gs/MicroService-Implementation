package com.regulareedge.compliancecoreservice.repository;

import com.regulareedge.compliancecoreservice.entity.DataCollectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataCollectionRequestRepository extends JpaRepository<DataCollectionRequest, Integer> {

    List<DataCollectionRequest> findByDataOwnerId(int dataOwnerId);

    List<DataCollectionRequest> findByCalendar_CalendarId(int calendarId);
}
