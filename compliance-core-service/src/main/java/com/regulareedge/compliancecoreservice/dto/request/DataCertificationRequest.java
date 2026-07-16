package com.regulareedge.compliancecoreservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class DataCertificationRequest {

    @NotNull(message = "requestId is required")
    @Positive(message = "requestId must be positive")
    private Integer requestId;

    @NotNull(message = "certifiedById is required")
    @Positive(message = "certifiedById must be positive")
    private Integer certifiedById;

    @NotNull(message = "certificationDate is required")
    private LocalDate certificationDate;

    @NotBlank(message = "certificationStatement is required")
    private String certificationStatement;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getCertifiedById() {
        return certifiedById;
    }

    public void setCertifiedById(Integer certifiedById) {
        this.certifiedById = certifiedById;
    }

    public LocalDate getCertificationDate() {
        return certificationDate;
    }

    public void setCertificationDate(LocalDate certificationDate) {
        this.certificationDate = certificationDate;
    }

    public String getCertificationStatement() {
        return certificationStatement;
    }

    public void setCertificationStatement(String certificationStatement) {
        this.certificationStatement = certificationStatement;
    }
}
