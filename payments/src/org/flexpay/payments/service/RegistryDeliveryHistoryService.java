package org.flexpay.payments.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public interface RegistryDeliveryHistoryService {
    /**
     * Read RegistryDeliveryHistory object by Stub
     *
     * @param registryDeliveryHistoryStub registryDeliveryHistory stub
     * @return RegistryDeliveryHistory object
     */
    @Nullable
    RegistryDeliveryHistory read(@NotNull Stub<RegistryDeliveryHistory> registryDeliveryHistoryStub);

    /**
     * Create RegistryDeliveryHistory object
     *
     * @param registryDeliveryHistory RegistryDeliveryHistory object
     */
	void create(@NotNull RegistryDeliveryHistory registryDeliveryHistory);

    /**
     * Update RegistryDeliveryHistory object
     *
     * @param registryDeliveryHistory  RegistryDeliveryHistory object
     */
	void update(@NotNull RegistryDeliveryHistory registryDeliveryHistory);

    /**
     * Delete RegistryDeliveryHistory object
     *
     * @param registryDeliveryHistoryStub  RegistryDeliveryHistory object
     */
	void delete(@NotNull Stub<RegistryDeliveryHistory> registryDeliveryHistoryStub);


     /**
     * List registry delivery histories in date range.
     *
     * @param pager Page
     * @param begin begin date is more {@link org.flexpay.common.persistence.registry.Registry#fromDate}
     * @param end end date is less {@link org.flexpay.common.persistence.registry.Registry#tillDate}
     * @return  list
     */
    @NotNull
    List<RegistryDeliveryHistory> listRegistryDeliveryHistories(@NotNull Page<RegistryDeliveryHistory> pager, @NotNull Date begin, @NotNull Date end);
}
