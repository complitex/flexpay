package org.flexpay.tc.actions.buildingattributes;

import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class BuildingAttributesEditAction extends FPActionSupport {
    @NotNull
    protected String doExecute() throws Exception {

        // TODO PROCESSING

        if (!isSubmit()) {
            
            return INPUT;
        }

        return REDIRECT_SUCCESS;
    }

    @NotNull
    protected String getErrorResult() {

        // TODO processing
        
        return INPUT;
    }
}
