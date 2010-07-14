package org.flexpay.payments.actions.registry;

import org.flexpay.common.persistence.filter.*;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.persistence.registry.RegistryProperties;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.PaymentPointFilter;
import org.flexpay.orgs.persistence.filters.RecipientOrganizationFilter;
import org.flexpay.orgs.persistence.filters.SenderOrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.EircRegistryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang.time.DateUtils.addDays;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.persistence.registry.RegistryFPFileType.FP_FORMAT;
import static org.flexpay.common.persistence.registry.RegistryFPFileType.MB_FORMAT;
import static org.flexpay.common.util.CollectionUtils.*;
import static org.flexpay.common.util.DateUtil.*;

public class RegistriesListAction extends AccountantAWPWithPagerActionSupport<Registry> {

	private SenderOrganizationFilter senderOrganizationFilter = new SenderOrganizationFilter();
	private RecipientOrganizationFilter recipientOrganizationFilter = new RecipientOrganizationFilter();
	private RegistryTypeFilter registryTypeFilter = new RegistryTypeFilter();
    private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();
	private Date fromDate = truncateDay(addDays(now(), -2));
	private Date tillDate = getEndOfThisDay(now());

	private List<Registry> registries = list();
    private RegistryFPFileType fpType;
    private RegistryFPFileType mbType;
    private Map<Long, Organization> orgs = map();

	private OrganizationService organizationService;
	private EircRegistryService eircRegistryService;
	private RegistryFPFileTypeService registryFPFileTypeService;
	private String moduleName;
	private FPFileService fileService;

	@NotNull
    @Override
	public String doExecute() throws Exception {

		List<ObjectFilter> filters = list(
				senderOrganizationFilter,
				recipientOrganizationFilter,
				registryTypeFilter,
				new FPModuleFilter(stub(fileService.getModuleByName(moduleName))),
				new BeginDateFilter(fromDate),
				new EndDateFilter(tillDate)
		);

        if (serviceProviderFilter != null && serviceProviderFilter.getSelectedId() != null && serviceProviderFilter.getSelectedId() > 0) {
            filters.add(serviceProviderFilter);
        }

		registries = eircRegistryService.findObjects(filters, getPager());
		if (log.isDebugEnabled()) {
			log.debug("Total registries found: {}", registries.size());
		}

        Set<Long> orgIds = set();

        for (Registry registry : registries) {
            orgIds.add(registry.getRecipientCode());
            orgIds.add(registry.getSenderCode());
        }

        if (orgs == null) {
            orgs = map();
        }

        List<Organization> orgsList = organizationService.readFull(orgIds, false);
        for (Organization organization : orgsList) {
            orgs.put(organization.getId(), organization);
        }

        if (log.isDebugEnabled()) {
            log.debug("Found {} unique organizations", orgs.size());
        }

        List<RegistryFPFileType> types = registryFPFileTypeService.findByCodes(list(FP_FORMAT, MB_FORMAT));
        for (RegistryFPFileType type : types) {
            if (FP_FORMAT == type.getCode()) {
                fpType = type;
            } else if (MB_FORMAT == type.getCode()) {
                mbType = type;
            }
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

	public List<Registry> getRegistries() {
		return registries;
	}

	public void setSenderOrganizationFilter(SenderOrganizationFilter senderOrganizationFilter) {
		this.senderOrganizationFilter = senderOrganizationFilter;
	}

	public void setRecipientOrganizationFilter(RecipientOrganizationFilter recipientOrganizationFilter) {
		this.recipientOrganizationFilter = recipientOrganizationFilter;
	}

	public void setRegistryTypeFilter(RegistryTypeFilter registryTypeFilter) {
		this.registryTypeFilter = registryTypeFilter;
	}

    public void setServiceProviderFilter(ServiceProviderFilter serviceProviderFilter) {
        this.serviceProviderFilter = serviceProviderFilter;
    }

    public void setFromDate(String dt) {
		fromDate = truncateDay(parseDate(dt, currentMonth()));
		log.debug("dt = {}, fromDate = {}", dt, fromDate);
	}

	public void setTillDate(String dt) {
		tillDate = getEndOfThisDay(parseDate(dt, now()));
		log.debug("dt = {}, tillDate = {}", dt, tillDate);
	}

    public Map<Long, Organization> getOrgs() {
        return orgs;
    }

    public RegistryFPFileType getFpType() {
        return fpType;
    }

    public RegistryFPFileType getMbType() {
        return mbType;
    }

	@Required
	public void setEircRegistryService(EircRegistryService eircRegistryService) {
		this.eircRegistryService = eircRegistryService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setRegistryFPFileTypeService(RegistryFPFileTypeService registryFPFileTypeService) {
		this.registryFPFileTypeService = registryFPFileTypeService;
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFileService(FPFileService fileService) {
		this.fileService = fileService;
	}
}
