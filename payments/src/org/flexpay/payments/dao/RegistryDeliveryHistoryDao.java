package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public interface RegistryDeliveryHistoryDao extends GenericDao<RegistryDeliveryHistory, Long> {
    /**
     * List registry delivery histories in date range.
     *
     * @param pager Page
     * @param begin begin date is more {@link org.flexpay.common.persistence.registry.Registry#fromDate}
     * @param end end date is less {@link org.flexpay.common.persistence.registry.Registry#tillDate}
     * @return
     */
    @NotNull
    List<RegistryDeliveryHistory> listRegistryDeliveryHistories(@NotNull Page<RegistryDeliveryHistory> pager, @NotNull Date begin, @NotNull Date end);

    /**
     * Delete registry delivery histories by registry id
     *
     * @param registryId registry id
     */
    void deleteByRegistry(Long registryId);
}
