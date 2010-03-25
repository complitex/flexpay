package org.flexpay.common.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataCorrection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CorrectionDao extends GenericDao<DataCorrection, Long> {

    @NotNull
    List<DataCorrection> find(Long internalObjectId, int type, Page<DataCorrection> pager);

}
