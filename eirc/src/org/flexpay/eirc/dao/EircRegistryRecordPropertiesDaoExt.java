package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;

public interface EircRegistryRecordPropertiesDaoExt {

	EircRegistryRecordProperties find(Long propertyId);

}
