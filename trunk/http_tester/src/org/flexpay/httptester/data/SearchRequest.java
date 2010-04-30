package org.flexpay.httptester.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SearchRequest {

    private DebtInfo debtInfo;
    private int debtInfoType;
    private String login;
    private String signature;

    public static class DebtInfo {

        private String requestId;
        private Integer searchType;
        private String searchCriteria;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public Integer getSearchType() {
            return searchType;
        }

        public void setSearchType(Integer searchType) {
            this.searchType = searchType;
        }

        public String getSearchCriteria() {
            return searchCriteria;
        }

        public void setSearchCriteria(String searchCriteria) {
            this.searchCriteria = searchCriteria;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                    append("requestId", requestId).
                    append("searchType", searchType).
                    append("searchCriteria", searchCriteria).
                    toString();
        }
    }

    public int getDebtInfoType() {
        return debtInfoType;
    }

    public void setDebtInfoType(int debtInfoType) {
        this.debtInfoType = debtInfoType;
    }

    public DebtInfo getDebtInfo() {
        return debtInfo;
    }

    public void setDebtInfo(DebtInfo debtInfo) {
        debtInfoType = 1;
        this.debtInfo = debtInfo;
    }

    public void setQuittanceDebtInfo(DebtInfo debtInfo) {
        debtInfoType = 2;
        this.debtInfo = debtInfo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("debtInfo", debtInfo).
                append("debtInfoType", debtInfoType).
                append("login", login).
                append("signature", signature).
                toString();
    }
}
