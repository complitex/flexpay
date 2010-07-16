package org.flexpay.payments.actions.request.data.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.actions.request.data.request.data.DebtInfo;
import org.flexpay.payments.actions.request.data.request.data.PayDebt;
import org.flexpay.payments.actions.request.data.request.data.RegistryComment;
import org.flexpay.payments.actions.request.data.request.data.ReversalPay;

public class DebtsRequest {

	private DebtInfo debtInfo;
    private PayDebt payDebt;
    private ReversalPay reversalPay;
    private RegistryComment registryComment;
    private String login;
    private String signature;

    private RequestType requestType = RequestType.SEARCH_QUITTANCE_DEBT_REQUEST;

    public DebtInfo getDebtInfo() {
        return debtInfo;
    }

    public void setDebtInfo(DebtInfo debtInfo) {
        requestType = RequestType.SEARCH_DEBT_REQUEST;
        this.debtInfo = debtInfo;
    }

    public void setQuittanceDebtInfo(DebtInfo debtInfo) {
        requestType = RequestType.SEARCH_QUITTANCE_DEBT_REQUEST;
        this.debtInfo = debtInfo;
    }

    public PayDebt getPayDebt() {
        return payDebt;
    }

    public void setPayDebt(PayDebt payDebt) {
        requestType = RequestType.PAY_DEBT_REQUEST;
        this.payDebt = payDebt;
    }

    public ReversalPay getReversalPay() {
        return reversalPay;
    }

    public void setReversalPay(ReversalPay reversalPay) {
        requestType = RequestType.REVERSAL_PAY_REQUEST;
        this.reversalPay = reversalPay;
    }

    public RegistryComment getRegistryComment() {
        return registryComment;
    }

    public void setRegistryComment(RegistryComment registryComment) {
        requestType = RequestType.REGISTRY_COMMENT_REQUEST;
        this.registryComment = registryComment;
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

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("debtInfo", debtInfo).
                append("payDebt", payDebt).
                append("reversalPay", reversalPay).
                append("registryComment", registryComment).
                append("login", login).
                append("signature", signature).
                append("requestType", requestType).
                toString();
    }
}
