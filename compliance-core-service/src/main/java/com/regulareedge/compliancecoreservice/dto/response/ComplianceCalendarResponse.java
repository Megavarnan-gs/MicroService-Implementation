package com.regulareedge.compliancecoreservice.dto.response;

import com.regulareedge.compliancecoreservice.common.enums.CalendarStatus;

import java.time.LocalDate;

public class ComplianceCalendarResponse {

    private int calendarId;
    private int obligationId;
    private LocalDate dueDate;
    private LocalDate dataCutOffDate;
    private int reminderDays;
    private CalendarStatus status;

    public ComplianceCalendarResponse() {
    }

    public ComplianceCalendarResponse(int calendarId, int obligationId, LocalDate dueDate, LocalDate dataCutOffDate,
                                       int reminderDays, CalendarStatus status) {
        this.calendarId = calendarId;
        this.obligationId = obligationId;
        this.dueDate = dueDate;
        this.dataCutOffDate = dataCutOffDate;
        this.reminderDays = reminderDays;
        this.status = status;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
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

    public CalendarStatus getStatus() {
        return status;
    }

    public void setStatus(CalendarStatus status) {
        this.status = status;
    }
}
