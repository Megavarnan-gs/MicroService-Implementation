package com.regulareedge.compliancecoreservice.dto.request;

import com.regulareedge.compliancecoreservice.common.enums.DataRequestStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateDataRequestStatusRequest {

    @NotNull(message = "requestId is required")
    @Positive(message = "requestId must be positive")
    private Integer requestId;

    @NotNull(message = "status is required")
    private DataRequestStatus status;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public DataRequestStatus getStatus() {
        return status;
    }

    public void setStatus(DataRequestStatus status) {
        this.status = status;
    }
}
