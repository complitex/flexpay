package org.flexpay.payments.reports.payments;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;

public class AccPaymentsReportData extends AccReportData {

    private List<PaymentDetails> detailses;

    @Override
    public JRDataSource getDataSource() {
        return new JRBeanCollectionDataSource(detailses);
    }

    public List<PaymentDetails> getDetailses() {
        return detailses;
    }

    public void setDetailses(List<PaymentDetails> detailses) {
        this.detailses = detailses;
    }

    public static class PaymentDetails {

        private Long objectId;
        private String divisionName;
        private String divisionAddress;
        private BigDecimal paymentKvartplata;
        private BigDecimal paymentDogs;
        private BigDecimal paymentGarage;
        private BigDecimal paymentWarming;
        private BigDecimal paymentHotWater;
        private BigDecimal paymentColdWater;
        private BigDecimal paymentSewer;
        private BigDecimal sum;

        private List<PaymentDetails> childDetailses;

        public Long getObjectId() {
            return objectId;
        }

        public void setObjectId(Long objectId) {
            this.objectId = objectId;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public void setDivisionName(String divisionName) {
            this.divisionName = divisionName;
        }

        public String getDivisionAddress() {
            return divisionAddress;
        }

        public void setDivisionAddress(String divisionAddress) {
            this.divisionAddress = divisionAddress;
        }

        public BigDecimal getPaymentKvartplata() {
            return paymentKvartplata;
        }

        public void setPaymentKvartplata(BigDecimal paymentKvartplata) {
            this.paymentKvartplata = paymentKvartplata;
        }

        public BigDecimal getPaymentDogs() {
            return paymentDogs;
        }

        public void setPaymentDogs(BigDecimal paymentDogs) {
            this.paymentDogs = paymentDogs;
        }

        public BigDecimal getPaymentGarage() {
            return paymentGarage;
        }

        public void setPaymentGarage(BigDecimal paymentGarage) {
            this.paymentGarage = paymentGarage;
        }

        public BigDecimal getPaymentWarming() {
            return paymentWarming;
        }

        public void setPaymentWarming(BigDecimal paymentWarming) {
            this.paymentWarming = paymentWarming;
        }

        public BigDecimal getPaymentHotWater() {
            return paymentHotWater;
        }

        public void setPaymentHotWater(BigDecimal paymentHotWater) {
            this.paymentHotWater = paymentHotWater;
        }

        public BigDecimal getPaymentColdWater() {
            return paymentColdWater;
        }

        public void setPaymentColdWater(BigDecimal paymentColdWater) {
            this.paymentColdWater = paymentColdWater;
        }

        public BigDecimal getPaymentSewer() {
            return paymentSewer;
        }

        public void setPaymentSewer(BigDecimal paymentSewer) {
            this.paymentSewer = paymentSewer;
        }

        public BigDecimal getSum() {
            return sum;
        }

        public void setSum(BigDecimal sum) {
            this.sum = sum;
        }

        public List<PaymentDetails> getChildDetailses() {
            return childDetailses;
        }

        public void setChildDetailses(List<PaymentDetails> childDetailses) {
            this.childDetailses = childDetailses;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                    append("objectId", objectId).
                    append("divisionName", divisionName).
                    append("divisionAddress", divisionAddress).
                    append("paymentKvartplata", paymentKvartplata).
                    append("paymentDogs", paymentDogs).
                    append("paymentGarage", paymentGarage).
                    append("paymentWarming", paymentWarming).
                    append("paymentHotWater", paymentHotWater).
                    append("paymentColdWater", paymentColdWater).
                    append("paymentSewer", paymentSewer).
                    append("sum", sum).
                    append("childDetailses", childDetailses).
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
                append("detailses", detailses).
                toString();
    }

}
