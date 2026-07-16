package com.regulareedge.compliancecoreservice.dto.response;

import com.regulareedge.compliancecoreservice.common.enums.CertificationStatus;

import java.time.LocalDate;

public class DataCertificationResponse {

    private int certificationId;
    private int requestId;
    private int certifiedById;
    private LocalDate certificationDate;
    private String certificationStatement;
    private CertificationStatus status;

    public DataCertificationResponse() {
    }

    public DataCertificationResponse(int certificationId, int requestId, int certifiedById,
                                      LocalDate certificationDate, String certificationStatement,
                                      CertificationStatus status) {
        this.certificationId = certificationId;
        this.requestId = requestId;
        this.certifiedById = certifiedById;
        this.certificationDate = certificationDate;
        this.certificationStatement = certificationStatement;
        this.status = status;
    }

    public int getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(int certificationId) {
        this.certificationId = certificationId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getCertifiedById() {
        return certifiedById;
    }

    public void setCertifiedById(int certifiedById) {
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

    public CertificationStatus getStatus() {
        return status;
    }

    public void setStatus(CertificationStatus status) {
        this.status = status;
    }
}
