package org.flexpay.payments.action.outerrequest.request.response.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RegistryInfo implements Serializable {

    private Long registryId;
    private Date registryDate;
    private String registryType;
    private Long recordsCount;
    private BigDecimal totalSum;
    private String registryComment;

    public Long getRegistryId() {
        return registryId;
    }

    public void setRegistryId(Long registryId) {
        this.registryId = registryId;
    }

    public Date getRegistryDate() {
        return registryDate;
    }

    public void setRegistryDate(Date registryDate) {
        this.registryDate = registryDate;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public Long getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(Long recordsCount) {
        this.recordsCount = recordsCount;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    public String getRegistryComment() {
        return registryComment;
    }

    public void setRegistryComment(String registryComment) {
        this.registryComment = registryComment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("registryId", registryId).
                append("registryDate", registryDate).
                append("registryType", registryType).
                append("recordsCount", recordsCount).
                append("totalSum", totalSum).
                append("registryComment", registryComment).
                toString();
    }
}
