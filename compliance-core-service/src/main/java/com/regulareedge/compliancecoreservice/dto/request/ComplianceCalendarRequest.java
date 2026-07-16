package com.regulareedge.compliancecoreservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class ComplianceCalendarRequest {

    @NotNull(message = "obligationId is required")
    @Positive(message = "obligationId must be positive")
    private Integer obligationId;

    @NotNull(message = "dueDate is required")
    private LocalDate dueDate;

    @NotNull(message = "dataCutOffDate is required")
    private LocalDate dataCutOffDate;

    @Min(value = 0, message = "reminderDays must be zero or positive")
    private int reminderDays;

    public Integer getObligationId() {
        return obligationId;
    }

    public void setObligationId(Integer obligationId) {
        this.obligationId = obligationId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDataCutOffDate() {
        return dataCutOffDate;
    }

    public void setDataCutOffDate(LocalDate dataCutOffDate) {
        this.dataCutOffDate = dataCutOffDate;
    }

    public int getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(int reminderDays) {
        this.reminderDays = reminderDays;
    }
}
