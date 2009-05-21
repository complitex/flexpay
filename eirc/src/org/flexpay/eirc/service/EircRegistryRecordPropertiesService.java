package org.flexpay.eirc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecordProperties;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;

public interface EircRegistryRecordPropertiesService {

	EircRegistryRecordProperties find(Stub<RegistryRecordProperties> stub);

}
