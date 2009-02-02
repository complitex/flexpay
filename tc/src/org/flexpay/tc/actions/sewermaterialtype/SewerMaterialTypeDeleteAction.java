package org.flexpay.tc.actions.sewermaterialtype;

import org.flexpay.bti.service.SewerMaterialTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

public class SewerMaterialTypeDeleteAction extends FPActionSupport {

    private Set<Long> objectIds = new HashSet<Long>();

    private SewerMaterialTypeService sewerMaterialTypeService;

    @NotNull
    protected String doExecute() throws Exception {

        sewerMaterialTypeService.disable(objectIds);

        return REDIRECT_SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return REDIRECT_SUCCESS;
    }

    public Set<Long> getObjectIds() {
        return objectIds;
    }

    public void setObjectIds(Set<Long> objectIds) {
        this.objectIds = objectIds;
    }

	@Required
	public void setSewerMaterialTypeService(SewerMaterialTypeService sewerMaterialTypeService) {
		this.sewerMaterialTypeService = sewerMaterialTypeService;
	}

}
