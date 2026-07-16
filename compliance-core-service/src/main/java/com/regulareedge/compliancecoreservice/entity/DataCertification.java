package com.regulareedge.compliancecoreservice.entity;

import com.regulareedge.compliancecoreservice.common.enums.CertificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "data_certification")
public class DataCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int certificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private DataCollectionRequest request;

    /** External user id (owned by auth-service) - plain int, not a JPA relationship. */
    private int certifiedById;

    private LocalDate certificationDate;
    private String certificationStatement;

    @Enumerated(EnumType.STRING)
    private CertificationStatus status;

    public int getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(int certificationId) {
        this.certificationId = certificationId;
    }

    public DataCollectionRequest getRequest() {
        return request;
    }

    public void setRequest(DataCollectionRequest request) {
        this.request = request;
    }

    /** Convenience accessor delegating to the associated DataCollectionRequest. */
    public int getRequestId() {
        return request != null ? request.getRequestId() : 0;
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
