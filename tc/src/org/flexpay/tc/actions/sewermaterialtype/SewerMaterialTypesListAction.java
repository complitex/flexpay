package org.flexpay.tc.actions.sewermaterialtype;

import org.flexpay.bti.persistence.SewerMaterialType;
import org.flexpay.bti.service.SewerMaterialTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class SewerMaterialTypesListAction extends FPActionSupport {

    private SewerMaterialTypeService sewerMaterialTypeService;

    private Page<SewerMaterialType> pager = new Page<SewerMaterialType>();
    private List<SewerMaterialType> sewerMaterialTypes = Collections.emptyList();


    @NotNull
    protected String doExecute() throws Exception {

        sewerMaterialTypes = sewerMaterialTypeService.listSewerMaterialTypes(pager);

        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {

        return SUCCESS;

    }

    @Required
    public void setSewerMaterialTypeService(SewerMaterialTypeService sewerMaterialTypeService) {
        this.sewerMaterialTypeService = sewerMaterialTypeService;
    }


    public List<SewerMaterialType> getSewerMaterialTypes() {
        return sewerMaterialTypes;
    }

    public Page<SewerMaterialType> getPager() {
        return pager;
    }

    public void setPager(Page<SewerMaterialType> pager) {
        this.pager = pager;
    }
}
