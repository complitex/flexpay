package org.flexpay.payments.action.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.filter.StringValueFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.util.StringUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.persistence.registry.RegistryContainer.*;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;

public class RegistryViewPageAction extends AccountantAWPActionSupport {

	private Registry registry = new Registry();

	protected ImportErrorTypeFilter importErrorTypeFilter = null;
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();
    private Map<Long, Organization> orgs = map();
    private String paymentNumber;
    private String paymentDate;
    private String commentary;

    private OrganizationService organizationService;
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
		registry = registryService.readWithContainers(stub);
		if (registry == null) {
			log.warn("Can't get registry with id {} from DB", stub.getId());
			addActionError(getText("payments.error.registry.cant_get_registry"));
			return REDIRECT_ERROR;
		}

        for (RegistryContainer registryContainer : registry.getContainers()) {
            List<String> containerData = StringUtil.splitEscapable(registryContainer.getData(), CONTAINER_DATA_DELIMITER, ESCAPE_SYMBOL);
            if (containerData != null && !containerData.isEmpty() && COMMENTARY_CONTAINER_TYPE.equals(containerData.get(0))) {
                paymentNumber = containerData.get(1);
                paymentDate = containerData.get(2);
                commentary = containerData.get(3);
                break;
            }
        }

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Prior listing actions took: {}", watch);
			watch.reset();
			watch.start();
		}

		recordStatusFilter.setRecordStatuses(recordStatusService.listAllStatuses());
		getImportErrorTypeFilter().init(classToTypeRegistry);
        importErrorTypeFilter.getErrorTypes().remove(ImportErrorTypeFilter.TYPE_NO_ERRORS);

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Import error type filter init: {}", watch);
		}

        if (orgs == null) {
            orgs = map();
        }

        List<Organization> orgsList = organizationService.readFull(list(registry.getRecipientCode(), registry.getSenderCode()), false);
        for (Organization organization : orgsList) {
            orgs.put(organization.getId(), organization);
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

    public int getErrorStatusCode() {
        return RegistryRecordStatus.PROCESSED_WITH_ERROR;
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

    public Map<Long, Organization> getOrgs() {
        return orgs;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getCommentary() {
        return commentary;
    }

    public long getFilterTypeTown() {
        return StringValueFilter.TYPE_TOWN;
    }

    public long getFilterTypeStreet() {
        return StringValueFilter.TYPE_STREET;
    }

    public long getFilterTypeBuilding() {
        return StringValueFilter.TYPE_BUILDING;
    }

    public long getFilterTypeApartment() {
        return StringValueFilter.TYPE_APARTMENT;
    }

    @Required
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
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
