package com.regulareedge.compliancecoreservice.entity;

import com.regulareedge.compliancecoreservice.common.enums.QualityCheckOutcome;
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
@Table(name = "data_quality_check")
public class DataQualityCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private DataCollectionRequest request;

    private String ruleName;
    private String ruleDescription;
    private String expectedValue;
    private String actualValue;

    @Enumerated(EnumType.STRING)
    private QualityCheckOutcome outcome;

    private LocalDate checkedDate;

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
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

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public QualityCheckOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(QualityCheckOutcome outcome) {
        this.outcome = outcome;
    }

    public LocalDate getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(LocalDate checkedDate) {
        this.checkedDate = checkedDate;
    }
}
