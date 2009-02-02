package org.flexpay.tc.actions.sewermaterialtype;

import org.flexpay.bti.persistence.SewerMaterialType;
import org.flexpay.bti.service.SewerMaterialTypeService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class SewerMaterialTypesListAction extends FPActionWithPagerSupport<SewerMaterialType> {

    private List<SewerMaterialType> sewerMaterialTypes = Collections.emptyList();

	private SewerMaterialTypeService sewerMaterialTypeService;

    @NotNull
    protected String doExecute() throws Exception {

        sewerMaterialTypes = sewerMaterialTypeService.listSewerMaterialTypes(getPager());

        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public List<SewerMaterialType> getSewerMaterialTypes() {
        return sewerMaterialTypes;
    }

	@Required
	public void setSewerMaterialTypeService(SewerMaterialTypeService sewerMaterialTypeService) {
		this.sewerMaterialTypeService = sewerMaterialTypeService;
	}

}
