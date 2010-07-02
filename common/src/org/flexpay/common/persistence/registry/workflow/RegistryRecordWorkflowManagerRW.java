package org.flexpay.common.persistence.registry.workflow;

import org.flexpay.common.dao.ImportErrorDaoExt;
import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.persistence.registry.RegistryRecordStatus.PROCESSED_WITH_ERROR;
import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Helper class for registry records workflow
 */
@Transactional(readOnly = true)
public class RegistryRecordWorkflowManagerRW extends RegistryRecordWorkflowManagerRO {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ImportErrorDaoExt errorDaoExt;
	private RegistryRecordDao recordDao;
    private RegistryRecordDaoExt recordDaoExt;

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
		errorDao.create(error);
	}

	@Transactional (readOnly = false)
	@Override
	public void setNextStatus(RegistryRecord record, RegistryRecordStatus status) throws TransitionNotAllowed {
		super.setNextStatus(record, status);
		recordDao.update(record);
	}

    @Transactional (readOnly = false)
    @Override
    public void setNextStatusForErrorRecords(@NotNull Collection<RegistryRecord> records) throws TransitionNotAllowed {

        if (records.isEmpty()) {
            log.debug("Records collection is empty");
            return;
        }

        Set<Long> errorIds = set();
        Set<Long> recordIds = set();

        for (RegistryRecord record : records) {

            List<Integer> allowedCodes = transitions.get(code(record));
            if (allowedCodes.size() < 1) {
                throw new TransitionNotAllowed("No success transition");
            }

            if (code(record) != PROCESSED_WITH_ERROR) {
                log.warn("Incorrect record status in record collection");
                return;
            }

            recordIds.add(record.getId());

            ImportError error = record.getImportError();
            if (error == null) {
                log.debug("Error is null, skipping");
                continue;
            }

            errorIds.add(error.getId());
        }


        errorDaoExt.disableErrors(errorIds);
        recordDaoExt.updateErrorStatus(recordIds, statusService.findByCode(RegistryRecordStatus.FIXED));

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
		return super.setInitialStatus(record);
	}

	@Required
	public void setRecordDao(RegistryRecordDao recordDao) {
		this.recordDao = recordDao;
	}

    @Required
    public void setErrorDaoExt(ImportErrorDaoExt errorDaoExt) {
        this.errorDaoExt = errorDaoExt;
    }

    @Required
    public void setRecordDaoExt(RegistryRecordDaoExt recordDaoExt) {
        this.recordDaoExt = recordDaoExt;
    }
}
