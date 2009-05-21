package org.flexpay.eirc.service.imp;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecordProperties;
import org.flexpay.eirc.dao.EircRegistryRecordPropertiesDaoExt;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.service.EircRegistryRecordPropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class EircRegistryRecordPropertiesServiceImpl implements EircRegistryRecordPropertiesService {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private EircRegistryRecordPropertiesDaoExt eircRegistryRecordPropertiesDaoExt;

	public EircRegistryRecordProperties find(Stub<RegistryRecordProperties> stub) {
		log.info("stub = {}", stub);
		return eircRegistryRecordPropertiesDaoExt.find(stub.getId());
	}

	@Required
	public void setEircRegistryRecordPropertiesDaoExt(EircRegistryRecordPropertiesDaoExt eircRegistryRecordPropertiesDaoExt) {
		this.eircRegistryRecordPropertiesDaoExt = eircRegistryRecordPropertiesDaoExt;
	}

}
