package org.flexpay.eirc.actions.quittance.demo;

import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class QuittancePayAction extends FPActionSupport {

    private String summ;

    @NotNull
    protected String doExecute() throws Exception {

        if (isSubmit()) {
            session.put("demoQuittancePayPerformed", "true");
            return REDIRECT_SUCCESS;
        }

        return INPUT;
    }

    @Override
    public void validate() {

    }

    @NotNull
    protected String getErrorResult() {
        return INPUT;
    }

    public void setSumm(String summ) {
        this.summ = summ;
    }
}
