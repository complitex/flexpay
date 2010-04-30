package org.flexpay.payments.actions.search.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SearchDebtsRequest {

    public final static int QUITTANCE_DEBT_REQUEST = 1;
    public final static int DEBT_REQUEST = 2;

	private DebtInfo debtInfo;
    private String login;
    private String signature;

    private int debtInfoType = QUITTANCE_DEBT_REQUEST;

    public DebtInfo getDebtInfo() {
        return debtInfo;
    }

    public void setDebtInfo(DebtInfo debtInfo) {
        debtInfoType = DEBT_REQUEST;
        this.debtInfo = debtInfo;
    }

    public void setQuittanceDebtInfo(DebtInfo debtInfo) {
        debtInfoType = QUITTANCE_DEBT_REQUEST;
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

    public int getDebtInfoType() {
        return debtInfoType;
    }

    public void setDebtInfoType(int debtInfoType) {
        this.debtInfoType = debtInfoType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("debtInfo", debtInfo).
                append("login", login).
                append("signature", signature).
                append("debtInfoType", debtInfoType).
                toString();
    }
}
