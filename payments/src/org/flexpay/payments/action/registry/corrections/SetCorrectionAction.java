package org.flexpay.payments.action.registry.corrections;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.action.OperatorAWPActionSupport;
import org.flexpay.payments.action.registry.data.RecordErrorsGroupView;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.persistence.registry.RegistryRecordStatus.PROCESSED_WITH_ERROR;
import static org.flexpay.common.util.CollectionUtils.list;

public class SetCorrectionAction extends OperatorAWPActionSupport {

	protected DomainObject object = new DomainObject();
    protected Registry registry = new Registry();
    protected RecordErrorsGroup group;
	protected RegistryRecord record = new RegistryRecord();
	protected String type;

	protected CorrectionsService correctionsService;
    protected RegistryService registryService;
	protected RegistryRecordService registryRecordService;
	protected ServiceTypeService serviceTypeService;
	protected OrganizationService organizationService;
    protected ClassToTypeRegistry classToTypeRegistry;
    protected RegistryRecordStatusService registryRecordStatusService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (object == null || object.isNew()) {
			log.warn("Incorrect object id");
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

        RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
        ImportErrorTypeFilter importErrorTypeFilter = new ImportErrorTypeFilter();
        List<RegistryRecord> records = list();
        boolean isGroup = registry != null && group != null;
        RecordErrorsGroupView groupView = null;

        List<ObjectFilter> filters = list();
        filters.add(recordStatusFilter);
        filters.add(importErrorTypeFilter);

        if (group != null) {

            if (registry == null || registry.isNew()) {
                log.warn("Incorrect registry id");
                addActionError(getText("payments.error.registry.invalid_registry_id"));
                return SUCCESS;
            }

            registry = registryService.read(stub(registry));

            RegistryRecordStatus status = registryRecordStatusService.findByCode(PROCESSED_WITH_ERROR);
            if (status == null) {
                log.warn("Can't get status by code from DB ({})", PROCESSED_WITH_ERROR);
                return SUCCESS;
            }
            recordStatusFilter.setSelectedId(status.getId());

            importErrorTypeFilter.setSelectedType(group.getErrorType());

            groupView = new RecordErrorsGroupView(group, classToTypeRegistry);

            records = registryRecordService.listRecords(registry, filters, groupView.getCriteria(classToTypeRegistry), groupView.getParams(classToTypeRegistry), new Page<RegistryRecord>(2000));
            if (records.isEmpty()) {
                log.warn("Records not found");
                return SUCCESS;
            }

        } else {

            if (record == null || record.isNew()) {
                log.warn("Incorrect registry record id");
                addActionError(getText("payments.error.registry.record.not_specified"));
                return SUCCESS;
            }

            Stub<RegistryRecord> stub = stub(record);
            record = registryRecordService.read(stub);
            if (record == null) {
                log.warn("Can't get registry record with id {} from DB", stub.getId());
                addActionError(getText("payments.error.registry.record.invalid_specified"));
                return SUCCESS;
            }

        }

        Stub<DataSourceDescription> stubDataSourceDescription = getDataSourceDescription(isGroup ? registry : record.getRegistry());

        if (isGroup) {
            record = records.get(0);
        }

        if (saveCorrection(record, stubDataSourceDescription) && record.getImportError() != null) {

            Long recordsCount = 0L;
            Page<RegistryRecord> pager = new Page<RegistryRecord>(2000);

            if (isGroup) {

                for (;;) {
                    if (records.isEmpty()) {
                        log.debug("Records list is empty!");
                        break;
                    }
                    registryRecordService.removeError(records);
                    records = registryRecordService.listRecords(registry, filters, groupView.getCriteria(classToTypeRegistry), groupView.getParams(classToTypeRegistry), pager);
                }

            } else {

                for (;;) {

                    log.debug("Find records");
                    records = registryRecordService.listRecords(record, type, pager);
                    int foundRecords = records.size();
                    log.debug("Found {}", foundRecords);
                    log.debug("Processing records from {} to {}", recordsCount, (recordsCount + foundRecords));

                    if (records.isEmpty()) {
                        log.debug("Records list is empty!");
                        break;
                    }

                    recordsCount += foundRecords;
                    log.debug("Total processed records {}", recordsCount);

                    registryRecordService.removeError(records);

                }
            }
        }

		return SUCCESS;
	}

    protected Stub<DataSourceDescription> getDataSourceDescription(Registry registry) {

        EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
        Organization organization = organizationService.readFull(props.getSenderStub());
        if (organization == null) {
            log.warn("Can't get organization with id {} from DB", props.getSenderStub().getId());
            addActionError(getText("payments.error.data_source_not_found"));
            return null;
        }

        return organization.sourceDescriptionStub();

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

    public DomainObject getObject() {
        return object;
    }

    public void setObject(DomainObject object) {
        this.object = object;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public RecordErrorsGroup getGroup() {
        return group;
    }

    public void setGroup(RecordErrorsGroup group) {
        this.group = group;
    }

    public RegistryRecord getRecord() {
        return record;
    }

    public void setRecord(RegistryRecord record) {
        this.record = record;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

    @Required
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Required
    public void setRegistryRecordService(RegistryRecordService registryRecordService) {
        this.registryRecordService = registryRecordService;
    }

    @Required
    public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
        this.classToTypeRegistry = classToTypeRegistry;
    }

    @Required
    public void setRegistryRecordStatusService(RegistryRecordStatusService registryRecordStatusService) {
        this.registryRecordStatusService = registryRecordStatusService;
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
