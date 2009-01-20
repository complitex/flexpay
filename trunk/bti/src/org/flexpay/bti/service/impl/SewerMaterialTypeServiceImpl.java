package org.flexpay.bti.service.impl;

import org.flexpay.bti.dao.SewerMaterialTypeDAO;
import org.flexpay.bti.persistence.SewerMaterialType;
import org.flexpay.bti.service.SewerMaterialTypeService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SewerMaterialTypeServiceImpl implements SewerMaterialTypeService {

    private SewerMaterialTypeDAO sewerMaterialTypeDAO;

    @Transactional(readOnly = false)
    public void save(@NotNull SewerMaterialType sewerMaterialType) {
        if (sewerMaterialType.isNew()) {
            sewerMaterialType.setId(null);
            sewerMaterialTypeDAO.create(sewerMaterialType);
        } else {
            sewerMaterialTypeDAO.update(sewerMaterialType);
        }
    }

    public SewerMaterialType getSewerMaterialType(@NotNull Stub<SewerMaterialType> stub) {
        return sewerMaterialTypeDAO.readFull(stub.getId());
    }

    @Transactional(readOnly = false)
    public void disable(@NotNull Set<Long> objectIds) {
        for (Long id : objectIds) {
            SewerMaterialType sewerMaterialType = sewerMaterialTypeDAO.read(id);
            if (null != sewerMaterialType) {
                sewerMaterialType.disable();
                sewerMaterialTypeDAO.update(sewerMaterialType);
            }
        }
    }

    public List<SewerMaterialType> listSewerMaterialTypes(Page<SewerMaterialType> pager) {
        return sewerMaterialTypeDAO.findSewerMaterialTypes(pager);
    }

    @Required
    public void setSewerMaterialTypeDAO(SewerMaterialTypeDAO sewerMaterialTypeDAO) {
        this.sewerMaterialTypeDAO = sewerMaterialTypeDAO;
    }
}
