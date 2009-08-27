package org.flexpay.common.service.imp;

import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.dao.registry.RegistryFPFileTypeDao;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class RegistryFPFileTypeServiceImpl implements RegistryFPFileTypeService {
    private RegistryFPFileTypeDao registryFPFileTypeDao;

    /**
     * Read RegistryFPFileType by id
     *
     * @param id ID
     * @return RegistryFPFileType object or <code>null</code>
     */
    @Nullable
    public RegistryFPFileType read(Long id) {
        return registryFPFileTypeDao.readFull(id);
    }

    /**
     * Read RegistryFPFileType by code
     *
     * @param code code of type
     * @return RegistryFPFileType object or <code>null</code>
     */
    public RegistryFPFileType findByCode(int code) {
        List<RegistryFPFileType> findRegistryFPFileTypes = registryFPFileTypeDao.findByCode(code);
        return findRegistryFPFileTypes.size() > 0? findRegistryFPFileTypes.get(0): null;
    }

    /**
     * @param registryFPFileTypeDao the registryFPFileTypeDao set
     */
    @Required
    public void setFpFileRegistryTypeDao(RegistryFPFileTypeDao registryFPFileTypeDao) {
        this.registryFPFileTypeDao = registryFPFileTypeDao;
    }
}
