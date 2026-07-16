package com.regulareedge.auditanalyticsservice.dto.response;

import com.regulareedge.auditanalyticsservice.common.enums.EvidenceStatus;

import java.time.LocalDate;

public class ControlEvidenceResponse {

    private int evidenceId;
    private int returnId;
    private String evidenceType;
    private String filePath;
    private int uploadedById;
    private LocalDate uploadedDate;
    private EvidenceStatus status;

    public ControlEvidenceResponse() {
    }

    public ControlEvidenceResponse(int evidenceId, int returnId, String evidenceType, String filePath,
                                    int uploadedById, LocalDate uploadedDate, EvidenceStatus status) {
        this.evidenceId = evidenceId;
        this.returnId = returnId;
        this.evidenceType = evidenceType;
        this.filePath = filePath;
        this.uploadedById = uploadedById;
        this.uploadedDate = uploadedDate;
        this.status = status;
    }

    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public String getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getUploadedById() {
        return uploadedById;
    }

    public void setUploadedById(int uploadedById) {
        this.uploadedById = uploadedById;
    }

    public LocalDate getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDate uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public EvidenceStatus getStatus() {
        return status;
    }

    public void setStatus(EvidenceStatus status) {
        this.status = status;
    }
}
