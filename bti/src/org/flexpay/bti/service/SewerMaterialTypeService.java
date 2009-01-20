package org.flexpay.bti.service;

import org.flexpay.bti.persistence.SewerMaterialType;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface SewerMaterialTypeService {

    @Secured({Roles.SEWER_MATERIAL_TYPE_ADD, Roles.SEWER_MATERIAL_TYPE_CHANGE})
    void save(@NotNull SewerMaterialType sewerMaterialType);

    @Secured(Roles.SEWER_MATERIAL_TYPE_READ)
    SewerMaterialType getSewerMaterialType(@NotNull Stub<SewerMaterialType> stub);

    @Secured(Roles.SEWER_MATERIAL_TYPE_DELETE)
    void disable(@NotNull Set<Long> objectIds);

    @Secured(Roles.SEWER_MATERIAL_TYPE_READ)
    List<SewerMaterialType> listSewerMaterialTypes(Page<SewerMaterialType> pager);
}
