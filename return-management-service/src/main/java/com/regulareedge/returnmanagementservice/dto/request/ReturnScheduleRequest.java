package com.regulareedge.returnmanagementservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ReturnScheduleRequest {

    @NotNull(message = "returnId is required")
    @Positive(message = "returnId must be positive")
    private Integer returnId;

    @NotBlank(message = "scheduleName is required")
    private String scheduleName;

    private String dataContent;

    @NotNull(message = "validatedById is required")
    @Positive(message = "validatedById must be positive")
    private Integer validatedById;

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        this.returnId = returnId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public Integer getValidatedById() {
        return validatedById;
    }

    public void setValidatedById(Integer validatedById) {
        this.validatedById = validatedById;
    }
}
