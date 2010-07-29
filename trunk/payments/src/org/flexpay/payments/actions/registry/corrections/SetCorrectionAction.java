package org.flexpay.payments.actions.registry.corrections;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.actions.OperatorAWPActionSupport;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class SetCorrectionAction extends OperatorAWPActionSupport {

	protected DomainObject object = new DomainObject();
	protected RegistryRecord record = new RegistryRecord();
	protected String type;

	protected CorrectionsService correctionsService;
	protected RegistryRecordService recordService;
	protected ServiceTypeService serviceTypeService;
	protected OrganizationService organizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (object == null || object.isNew()) {
			log.warn("Incorrect object id");
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

		if (record == null || record.isNew()) {
			log.warn("Incorrect registry record id");
			addActionError(getText("error.registry.record.not_specified"));
			return SUCCESS;
		}

		Stub<RegistryRecord> stub = stub(record);
		record = recordService.read(stub);
		if (record == null) {
			log.warn("Can't get registry record with id {} from DB", stub.getId());
			addActionError(getText("error.registry.record.invalid_specified"));
			return SUCCESS;
		}

		EircRegistryProperties props = (EircRegistryProperties) record.getRegistry().getProperties();
		Organization organization = organizationService.readFull(props.getSenderStub());
		if (organization == null) {
			log.warn("Can't get organization with id {} from DB", props.getSenderStub().getId());
			addActionError(getText("error.eirc.data_source_not_found"));
			return SUCCESS;
		}

        if (saveCorrection(record, organization.sourceDescriptionStub()) && record.getImportError() != null) {

            Page<RegistryRecord> pager = new Page<RegistryRecord>(2000);
            Long recordsCount = 0L;

            for (;;) {

                log.debug("Find records");
                List<RegistryRecord> records = recordService.listRecords(record, type, pager);
                int foundRecords = records.size();
                log.debug("Found {}", foundRecords);
                log.debug("Processing records from {} to {}", recordsCount, (recordsCount + foundRecords));

                if (records.isEmpty()) {
                    log.debug("Records list is empty!");
                    break;
                }

                recordsCount += foundRecords;
                log.debug("Total processed records {}", recordsCount);

                recordService.removeError(records);

            }
        }

		return SUCCESS;
	}

	protected boolean saveCorrection(RegistryRecord record, Stub<DataSourceDescription> sd) {
		return true;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setObject(DomainObject object) {
		this.object = object;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setRecordService(RegistryRecordService recordService) {
		this.recordService = recordService;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
