package com.regulareedge.compliancecoreservice.dto.request;

import com.regulareedge.compliancecoreservice.common.enums.QualityCheckOutcome;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class DataQualityCheckRequest {

    @NotNull(message = "requestId is required")
    @Positive(message = "requestId must be positive")
    private Integer requestId;

    @NotBlank(message = "ruleName is required")
    private String ruleName;

    private String ruleDescription;
    private String expectedValue;
    private String actualValue;

    @NotNull(message = "outcome is required")
    private QualityCheckOutcome outcome;

    @NotNull(message = "checkedDate is required")
    private LocalDate checkedDate;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
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
