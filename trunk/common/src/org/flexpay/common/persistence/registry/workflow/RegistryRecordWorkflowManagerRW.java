package org.flexpay.common.persistence.registry.workflow;

import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.persistence.ImportError;
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
	public void startProcessing(RegistryRecord record) throws TransitionNotAllowed {
		super.startProcessing(record);
	}

	@Transactional (readOnly = false)
	@Override
	public void setNextErrorStatus(RegistryRecord record) throws TransitionNotAllowed {
		super.setNextErrorStatus(record);
	}

	@Transactional (readOnly = false)
	@Override
	public void setNextErrorStatus(RegistryRecord record, ImportError error) throws TransitionNotAllowed {
		super.setNextErrorStatus(record, error);
	}

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

	@Transactional (readOnly = false)
	@Override
	public RegistryRecord removeError(RegistryRecord record) {
		return super.removeError(record);
	}

	@Transactional (readOnly = false)
	@Override
	public RegistryRecord setInitialStatus(RegistryRecord record) throws TransitionNotAllowed {
		return super.setInitialStatus(record);	//To change body of overridden methods use File | Settings | File Templates.
	}

	@Required
	public void setRecordDao(RegistryRecordDao recordDao) {
		this.recordDao = recordDao;
	}
}
