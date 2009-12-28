package org.flexpay.payments.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

public class RegistryRecordsListAction extends FPActionWithPagerSupport<RegistryRecord> {

	private Registry registry = new Registry();
	private List<RegistryRecord> records = list();

	protected ImportErrorTypeFilter importErrorTypeFilter = null;
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();

	private ServiceTypeService serviceTypeService;
	private RegistryRecordService registryRecordService;
	private ClassToTypeRegistry classToTypeRegistry;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (registry == null || registry.isNew()) {
			log.warn("Incorrect registry id");
			addActionError(getText("payments.error.registry.incorrect_registry_id"));
			return SUCCESS;
		}

		StopWatch watch = new StopWatch();
		if (log.isDebugEnabled()) {
			watch.start();
		}

		if (importErrorTypeFilter == null) {
			importErrorTypeFilter = new ImportErrorTypeFilter();
		}

		importErrorTypeFilter.init(classToTypeRegistry);

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Import error type filter init: {}", watch);
			watch.reset();
		}

		if (log.isDebugEnabled()) {
			watch.start();
		}

		records = registryRecordService.listRecords(registry, importErrorTypeFilter, recordStatusFilter, getPager());
		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Time spent listing records: {}", watch);
			log.debug("Total records found: {}", records.size());
		}

		return SUCCESS;
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

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = serviceTypeService.read(stub(typeStub));
		ServiceTypeNameTranslation name = getTranslation(type.getTypeNames());
		return getTranslationName(type.getTypeNames());
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public void setImportErrorTypeFilter(ImportErrorTypeFilter importErrorTypeFilter) {
		this.importErrorTypeFilter = importErrorTypeFilter;
	}

	public void setRecordStatusFilter(RegistryRecordStatusFilter recordStatusFilter) {
		this.recordStatusFilter = recordStatusFilter;
	}

	public List<RegistryRecord> getRecords() {
		return records;
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
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
