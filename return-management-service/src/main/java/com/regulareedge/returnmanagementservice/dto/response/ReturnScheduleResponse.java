package com.regulareedge.returnmanagementservice.dto.response;

public class ReturnScheduleResponse {

    private int scheduleId;
    private int returnId;
    private String scheduleName;
    private String dataContent;
    private int validatedById;
    private String status;

    public ReturnScheduleResponse() {
    }

    public ReturnScheduleResponse(int scheduleId, int returnId, String scheduleName, String dataContent,
                                   int validatedById, String status) {
        this.scheduleId = scheduleId;
        this.returnId = returnId;
        this.scheduleName = scheduleName;
        this.dataContent = dataContent;
        this.validatedById = validatedById;
        this.status = status;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
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

    public int getValidatedById() {
        return validatedById;
    }

    public void setValidatedById(int validatedById) {
        this.validatedById = validatedById;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
