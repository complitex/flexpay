package org.flexpay.payments.actions.request.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DebtsRequest {

    public final static int SEARCH_QUITTANCE_DEBT_REQUEST = 1;
    public final static int SEARCH_DEBT_REQUEST = 2;
    public final static int PAY_DEBT_REQUEST = 3;

	private DebtInfo debtInfo;
    private PayDebt payDebt;
    private String login;
    private String signature;

    private int debtRequestType = SEARCH_QUITTANCE_DEBT_REQUEST;

    public DebtInfo getDebtInfo() {
        return debtInfo;
    }

    public void setDebtInfo(DebtInfo debtInfo) {
        debtRequestType = SEARCH_DEBT_REQUEST;
        this.debtInfo = debtInfo;
    }

    public void setQuittanceDebtInfo(DebtInfo debtInfo) {
        debtRequestType = SEARCH_QUITTANCE_DEBT_REQUEST;
        this.debtInfo = debtInfo;
    }

    public PayDebt getPayDebt() {
        return payDebt;
    }

    public void setPayDebt(PayDebt payDebt) {
        debtRequestType = PAY_DEBT_REQUEST;
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

    public int getDebtRequestType() {
        return debtRequestType;
    }

    public void setDebtRequestType(int debtRequestType) {
        this.debtRequestType = debtRequestType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("debtInfo", debtInfo).
                append("payDebt", payDebt).
                append("login", login).
                append("signature", signature).
                append("debtRequestType", debtRequestType).
                toString();
    }
}
