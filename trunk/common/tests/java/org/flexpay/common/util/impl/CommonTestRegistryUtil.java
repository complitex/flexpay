package org.flexpay.common.util.impl;

import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.TestRegistryUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CommonTestRegistryUtil implements TestRegistryUtil {

    @Autowired
    private RegistryService registryService;

    @Autowired
    private RegistryDao registryDao;

    @Override
    public void delete(@NotNull Registry registry) {
        deleteRecordDependences(Stub.stub(registry));
        registryService.deleteRecords(Stub.stub(registry));
        deleteRegistryDependences(Stub.stub(registry));
        registryService.delete(registry);
    }


    @Transactional(readOnly = false)
    protected void deleteRecordDependences(@NotNull Stub<Registry> stub) {
        registryDao.deleteRecordContainers(stub.getId());
        registryDao.deleteRecordProperties(stub.getId());
	}

    @Transactional(readOnly = false)
    protected void deleteRegistryDependences(@NotNull Stub<Registry> stub) {

    }
}
