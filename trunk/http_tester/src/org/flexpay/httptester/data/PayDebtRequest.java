package org.flexpay.httptester.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PayDebtRequest {

    private PayDebt payDebt;
    private String login;
    private String signature;

    public PayDebt getPayDebt() {
        return payDebt;
    }

    public void setPayDebt(PayDebt payDebt) {
        this.payDebt = payDebt;
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

    public static class PayDebt {

        private String requestId;
        private Integer totalPaySum;
        private ServicePayDetails[] servicePayDetailses;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public Integer getTotalPaySum() {
            return totalPaySum;
        }

        public void setTotalPaySum(Integer totalPaySum) {
            this.totalPaySum = totalPaySum;
        }

        public ServicePayDetails[] getServicePayDetailses() {
            return servicePayDetailses;
        }

        public void setServicePayDetailses(ServicePayDetails[] servicePayDetailses) {
            this.servicePayDetailses = servicePayDetailses;
        }

        public static class ServicePayDetails {

            private Long serviceId;
            private String serviceProviderAccount;
            private Integer paySum;

            public Long getServiceId() {
                return serviceId;
            }

            public void setServiceId(Long serviceId) {
                this.serviceId = serviceId;
            }

            public String getServiceProviderAccount() {
                return serviceProviderAccount;
            }

            public void setServiceProviderAccount(String serviceProviderAccount) {
                this.serviceProviderAccount = serviceProviderAccount;
            }

            public Integer getPaySum() {
                return paySum;
            }

            public void setPaySum(Integer paySum) {
                this.paySum = paySum;
            }

            @Override
            public String toString() {
                return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                        append("serviceId", serviceId).
                        append("serviceProviderAccount", serviceProviderAccount).
                        append("paySum", paySum).
                        toString();
            }
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                    append("requestId", requestId).
                    append("totalPaySum", totalPaySum).
                    append("servicePayDetailses", servicePayDetailses).
                    toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("payDebt", payDebt).
                append("login", login).
                append("signature", signature).
                toString();
    }
}
