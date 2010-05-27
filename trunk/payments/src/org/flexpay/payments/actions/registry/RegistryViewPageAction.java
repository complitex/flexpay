package org.flexpay.payments.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class RegistryViewPageAction extends AccountantAWPActionSupport {

	private Registry registry = new Registry();

	protected ImportErrorTypeFilter importErrorTypeFilter = null;
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();

	private RegistryService registryService;
	private ServiceTypeService serviceTypeService;
	private RegistryRecordStatusService recordStatusService;
	private ClassToTypeRegistry classToTypeRegistry;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (registry == null || registry.isNew()) {
			log.warn("Incorrect registry id");
			addActionError(getText("payments.error.registry.incorrect_registry_id"));
			return REDIRECT_ERROR;
		}

		StopWatch watch = new StopWatch();
		if (log.isDebugEnabled()) {
			watch.start();
		}

		Stub<Registry> stub = stub(registry);
		registry = registryService.read(stub);

		if (registry == null) {
			log.warn("Can't get registry with id {} from DB", stub.getId());
			addActionError(getText("payments.error.registry.cant_get_registry"));
			return REDIRECT_ERROR;
		}
		registryService.checkRegistryErrorsNumber(registry);

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Prior listing actions took: {}", watch);
			watch.reset();
			watch.start();
		}

		recordStatusFilter.setRecordStatuses(recordStatusService.listAllStatuses());
		getImportErrorTypeFilter().init(classToTypeRegistry);

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Import error type filter init: {}", watch);
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
		return REDIRECT_ERROR;
	}

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = serviceTypeService.read(stub(typeStub));
		return getTranslationName(type.getTypeNames());
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public ImportErrorTypeFilter getImportErrorTypeFilter() {
		if (importErrorTypeFilter == null) {
			importErrorTypeFilter = new ImportErrorTypeFilter();
		}
		return importErrorTypeFilter;
	}

	public RegistryRecordStatusFilter getRecordStatusFilter() {
		return recordStatusFilter;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

	@Required
	public void setRecordStatusService(RegistryRecordStatusService recordStatusService) {
		this.recordStatusService = recordStatusService;
	}
}
