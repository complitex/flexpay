package org.flexpay.eirc.action.eircaccount;

import org.jetbrains.annotations.NotNull;

public class EircAccountsListPageAction extends EircAccountAction {

    @NotNull
    @Override
    protected String doExecute() throws Exception {
        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

}
