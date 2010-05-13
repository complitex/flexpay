package org.flexpay.common.persistence.registry.workflow;

import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Helper class for registry records workflow
 */
@Transactional(readOnly = true)
public class RegistryRecordWorkflowManagerRW extends RegistryRecordWorkflowManagerRO {
	private RegistryRecordDao recordDao;

	@Transactional (readOnly = false)
	@Override
	public void setNextStatus(RegistryRecord record, RegistryRecordStatus status) throws TransitionNotAllowed {
		super.setNextStatus(record, status);
		recordDao.update(record);
	}

	@Transactional (readOnly = false)
	@Override
	public void setNextSuccessStatus(Collection<RegistryRecord> records) throws TransitionNotAllowed {
		super.setNextSuccessStatus(records);
	}

	@Required
	public void setRecordDao(RegistryRecordDao recordDao) {
		this.recordDao = recordDao;
	}
}
