package org.flexpay.httptester.outerrequest.request.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class GetRegistryListResponse extends Response {

    private List<RegistryInfo> registryInfos = new ArrayList<RegistryInfo>();

    public List<RegistryInfo> getRegistryInfos() {
        return registryInfos;
    }

    public void setRegistryInfos(List<RegistryInfo> registryInfos) {
        this.registryInfos = registryInfos;
    }

    public void addRegistryInfo(RegistryInfo registryInfo) {
        registryInfos.add(registryInfo);
    }

    public static class RegistryInfo {

        private String registryId;
        private String registryDate;
        private String registryType;
        private String recordsCount;
        private String totalSum;
        private String registryComment;

        public String getRegistryId() {
            return registryId;
        }

        public void setRegistryId(String registryId) {
            this.registryId = registryId;
        }

        public String getRegistryDate() {
            return registryDate;
        }

        public void setRegistryDate(String registryDate) {
            this.registryDate = registryDate;
        }

        public String getRegistryType() {
            return registryType;
        }

        public void setRegistryType(String registryType) {
            this.registryType = registryType;
        }

        public String getRecordsCount() {
            return recordsCount;
        }

        public void setRecordsCount(String recordsCount) {
            this.recordsCount = recordsCount;
        }

        public String getTotalSum() {
            return totalSum;
        }

        public void setTotalSum(String totalSum) {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("signature", signature).
                append("registryInfos", registryInfos).
                toString();
    }
}
