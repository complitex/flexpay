package org.flexpay.payments.service.impl;

import org.flexpay.payments.service.RegistryDeliveryHistoryService;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.flexpay.payments.dao.RegistryDeliveryHistoryDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;

@Transactional (readOnly = true)
public class RegistryDeliveryHistoryServiceImpl implements RegistryDeliveryHistoryService {
    private Logger log = LoggerFactory.getLogger(getClass());

    private RegistryDeliveryHistoryDao registryDeliveryHistoryDao;

     /**
     * Read RegistryDeliveryHistory object by Stub
     *
     * @param registryDeliveryHistoryStub registryDeliveryHistory stub
     * @return RegistryDeliveryHistory object
     */
    public RegistryDeliveryHistory read(@NotNull Stub<RegistryDeliveryHistory> registryDeliveryHistoryStub) {
        return registryDeliveryHistoryDao.readFull(registryDeliveryHistoryStub.getId());
    }

    /**
     * Create RegistryDeliveryHistory object
     *
     * @param registryDeliveryHistory RegistryDeliveryHistory object
     */
    @Transactional(readOnly = false)
    public void create(@NotNull RegistryDeliveryHistory registryDeliveryHistory) {
        registryDeliveryHistory.setId(null);
        registryDeliveryHistoryDao.create(registryDeliveryHistory);
    }

    /**
     * Update RegistryDeliveryHistory object
     *
     * @param registryDeliveryHistory  RegistryDeliveryHistory object
     */
    @Transactional (readOnly = false)
    public void update(@NotNull RegistryDeliveryHistory registryDeliveryHistory) {
        registryDeliveryHistoryDao.update(registryDeliveryHistory);
    }

    /**
     * Delete RegistryDeliveryHistory object
     *
     * @param registryDeliveryHistoryStub  RegistryDeliveryHistory object
     */
    @Transactional (readOnly = false)
    public void delete(@NotNull Stub<RegistryDeliveryHistory> registryDeliveryHistoryStub) {
        RegistryDeliveryHistory registryDeliveryHistory = registryDeliveryHistoryDao.read(registryDeliveryHistoryStub.getId());
        if (registryDeliveryHistory == null) {
            log.debug("Can't find registry delivery history with id {}", registryDeliveryHistoryStub.getId());
			return;
        }
        registryDeliveryHistoryDao.delete(registryDeliveryHistory);
    }

    /**
     * List registry delivery histories in date range.
     *
     * @param begin begin date is more {@link org.flexpay.common.persistence.registry.Registry#fromDate}
     * @param end end date is less {@link org.flexpay.common.persistence.registry.Registry#tillDate}
     * @return  list
     */
    @NotNull
    public List<RegistryDeliveryHistory> listRegistryDeliveryHistories(@NotNull Page<RegistryDeliveryHistory> pager, @NotNull Date begin, @NotNull Date end) {
        return registryDeliveryHistoryDao.listRegistryDeliveryHistories(pager, begin, end);
    }

    @Required
    public void setRegistryDeliveryHistoryDao(RegistryDeliveryHistoryDao registryDeliveryHistoryDao) {
        this.registryDeliveryHistoryDao = registryDeliveryHistoryDao;
    }
}
