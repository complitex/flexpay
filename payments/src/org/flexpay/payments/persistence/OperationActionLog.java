package org.flexpay.payments.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.orgs.persistence.Cashbox;

import java.util.Date;

public class OperationActionLog extends DomainObject {

    public static int SEARCH_BY_ADDRESS = 1;
    public static int SEARCH_BY_QUITTANCE_NUMBER = 2;
    public static int SEARCH_BY_EIRC_ACCOUNT = 3;
    public static int PRINT_QUITTANCE = 4;

    private Date actionDate = new Date();
    private String userName;
    private Integer action;
    private String actionString = "";

    private Cashbox cashbox;

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getActionString() {
        return actionString;
    }

    public void setActionString(String actionString) {
        this.actionString = actionString;
    }

    public Cashbox getCashbox() {
        return cashbox;
    }

    public void setCashbox(Cashbox cashbox) {
        this.cashbox = cashbox;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("id", id).
                append("actionDate", actionDate).
                append("userName", userName).
                append("action", action).
                append("actionString", actionString).
                append("cashboxId", cashbox == null ? "null" : cashbox.getId()).
                toString();
    }
}
