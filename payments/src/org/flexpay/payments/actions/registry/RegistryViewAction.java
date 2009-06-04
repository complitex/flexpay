package org.flexpay.payments.actions.registry;

import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.actions.CashboxCookieWithPagerActionSupport;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class RegistryViewAction extends CashboxCookieWithPagerActionSupport<RegistryRecord> {

	private Registry registry = new Registry();
	private List<RegistryRecord> records = Collections.emptyList();

	private ImportErrorTypeFilter importErrorTypeFilter = new ImportErrorTypeFilter();
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();

	private RegistryService registryService;
	private ServiceTypeService serviceTypeService;
	private RegistryRecordService registryRecordService;
	private ClassToTypeRegistry classToTypeRegistry;

	@NotNull
	public String doExecute() throws Exception {
		if (registry.getId() == null) {
			addActionError("No registryId specified, give up.");
			return ERROR;
		}
		importErrorTypeFilter.init(classToTypeRegistry);
		registry = registryService.read(stub(registry));
		records = registryRecordService.listRecords(registry, importErrorTypeFilter, recordStatusFilter, getPager());

		log.info(String.format("pager: size %d, total %d, first %d",
				getPager().getPageSize(), getPager().getTotalNumberOfElements(), getPager().getThisPageFirstElementNumber()));

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
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = serviceTypeService.read(stub(typeStub));
		ServiceTypeNameTranslation name = getTranslation(type.getTypeNames());
		return name == null ? "Unknown" : name.getName();
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public ImportErrorTypeFilter getImportErrorTypeFilter() {
		return importErrorTypeFilter;
	}

	public void setImportErrorTypeFilter(ImportErrorTypeFilter importErrorTypeFilter) {
		this.importErrorTypeFilter = importErrorTypeFilter;
	}

	public RegistryRecordStatusFilter getRecordStatusFilter() {
		return recordStatusFilter;
	}

	public void setRecordStatusFilter(RegistryRecordStatusFilter recordStatusFilter) {
		this.recordStatusFilter = recordStatusFilter;
	}

	public List<RegistryRecord> getRecords() {
		return records;
	}

	public void setRecords(List<RegistryRecord> records) {
		this.records = records;
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
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
