package com.regulareedge.compliancecoreservice.dto.response;

import com.regulareedge.compliancecoreservice.common.enums.QualityCheckOutcome;

import java.time.LocalDate;

public class DataQualityCheckResponse {

    private int checkId;
    private int requestId;
    private String ruleName;
    private String ruleDescription;
    private String expectedValue;
    private String actualValue;
    private QualityCheckOutcome outcome;
    private LocalDate checkedDate;

    public DataQualityCheckResponse() {
    }

    public DataQualityCheckResponse(int checkId, int requestId, String ruleName, String ruleDescription,
                                     String expectedValue, String actualValue, QualityCheckOutcome outcome,
                                     LocalDate checkedDate) {
        this.checkId = checkId;
        this.requestId = requestId;
        this.ruleName = ruleName;
        this.ruleDescription = ruleDescription;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
        this.outcome = outcome;
        this.checkedDate = checkedDate;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
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
