package org.flexpay.eirc.actions.quittance.demo;

import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang.StringUtils;

public class QuittancePaySearchAction extends FPActionSupport {

    private String quittanceNumber;

    @NotNull
    protected String doExecute() throws Exception {

        if (isSubmit()) {

            // chechking whether quittance number is proper
            if (quittanceNumber.equals("09002311489-10/2008-012")) {

                String flag = (String) session.get("demoQuittancePayPerformed");

                if (flag != null && flag.equals("true")) {
                    addActionError(getText("eirc.quittances.demo.quittance_pay_search.already_payed"));
                    return INPUT;
                }

                return REDIRECT_SUCCESS;
            } else {
                addActionError(getText("eirc.error.quittance.no_quittance_found"));
                return INPUT;
            }
        }

        return INPUT;
    }

    @Override
    public void validate() {

        if (isSubmit()) {
            if (StringUtils.isEmpty(quittanceNumber)) {
                addActionError(getText("eirc.error.quittance.invalid_number"));
            }
        }
    }

    @NotNull
    protected String getErrorResult() {
        return INPUT;
    }

    public void setQuittanceNumber(String quittanceNumber) {
        this.quittanceNumber = quittanceNumber;
    }
}
