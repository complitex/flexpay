package org.flexpay.payments.reports.payments;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class AccPaymentsRegistriesReportData extends AccReportData {

    private List<ServiceProviderInfo> serviceProviderInfos = list();

    @Override
    public JRDataSource getDataSource() {
        return new JRBeanCollectionDataSource(serviceProviderInfos);
    }

    public List<ServiceProviderInfo> getServiceProviderInfos() {
        return serviceProviderInfos;
    }

    public void setServiceProviderInfos(List<ServiceProviderInfo> serviceProviderInfos) {
        this.serviceProviderInfos = serviceProviderInfos;
    }

    public static class ServiceProviderInfo implements Serializable {

        private String serviceProviderName = "";
        private List<RegistryInfo> infos = list();
        private List<ServiceProviderInfo> serviceProviderInfos = list();

        public List<ServiceProviderInfo> getServiceProviderInfos() {
            return serviceProviderInfos;
        }

        public void setServiceProviderInfos(List<ServiceProviderInfo> serviceProviderInfos) {
            this.serviceProviderInfos = serviceProviderInfos;
        }

        public String getServiceProviderName() {
            return serviceProviderName;
        }

        public void setServiceProviderName(String serviceProviderName) {
            this.serviceProviderName = serviceProviderName;
        }

        public List<RegistryInfo> getInfos() {
            return infos;
        }

        public void setInfos(List<RegistryInfo> infos) {
            this.infos = infos;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).
                    append("serviceProviderName", serviceProviderName).
                    append("infos", infos).
                    toString();
        }
    }

    public static class RegistryInfo implements Serializable {

        private Long id;
        private Long registryNumber = 0L;
        private String creationDate = "";
        private String sender = "";
        private String recipient = "";
        private Long recordsNumber = 0L;
        private BigDecimal sum = new BigDecimal("0.00");
        private String commentary = "";

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getRegistryNumber() {
            return registryNumber;
        }

        public void setRegistryNumber(Long registryNumber) {
            this.registryNumber = registryNumber;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getCommentary() {
            return commentary;
        }

        public void setCommentary(String commentary) {
            this.commentary = commentary;
        }

        public Long getRecordsNumber() {
            return recordsNumber;
        }

        public void setRecordsNumber(Long recordsNumber) {
            this.recordsNumber = recordsNumber;
        }

        public BigDecimal getSum() {
            return sum;
        }

        public void setSum(BigDecimal sum) {
            this.sum = sum;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).
                    append("id", id).
                    append("registryNumber", registryNumber).
                    append("creationDate", creationDate).
                    append("sender", sender).
                    append("recipient", recipient).
                    append("commentary", commentary).
                    append("recordsNumber", recordsNumber).
                    append("sum", sum).
                    toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("beginDate", beginDate).
                append("endDate", endDate).
                append("paymentCollectorOrgName", paymentCollectorOrgName).
                append("paymentCollectorOrgAddress", paymentCollectorOrgAddress).
                append("accountantFio", accountantFio).
                append("creationDate", creationDate).
                append("serviceProviderInfos", serviceProviderInfos).
                toString();
    }
}
