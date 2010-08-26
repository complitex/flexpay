package org.flexpay.payments.reports.payments;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AccPaymentsRegistriesReportData extends AccReportData {

    private String serviceProvider;
    private List<RegistryInfo> infos;

    @Override
    public JRDataSource getDataSource() {
        return new JRBeanCollectionDataSource(infos);
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public List<RegistryInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<RegistryInfo> infos) {
        this.infos = infos;
    }

    public static class RegistryInfo {

        private Long id;
        private Date creationDate;
        private String sender;
        private String recipient;
        private String commentary;
        private Long recordsNumber;
        private BigDecimal sum;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Date getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(Date creationDate) {
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
            return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                    append("id", id).
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
                append("serviceProvider", serviceProvider).
                append("infos", infos).
                toString();
    }
}
