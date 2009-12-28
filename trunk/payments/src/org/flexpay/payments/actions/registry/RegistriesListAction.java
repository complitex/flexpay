package org.flexpay.payments.actions.registry;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.*;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.persistence.registry.RegistryProperties;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.RecipientOrganizationFilter;
import org.flexpay.orgs.persistence.filters.SenderOrganizationFilter;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.EircRegistryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.time.DateUtils.addDays;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.*;

public class RegistriesListAction extends FPActionWithPagerSupport<Registry> {

	private SenderOrganizationFilter senderOrganizationFilter = new SenderOrganizationFilter();
	private RecipientOrganizationFilter recipientOrganizationFilter = new RecipientOrganizationFilter();
	private RegistryTypeFilter registryTypeFilter = new RegistryTypeFilter();
	private Date fromDate = truncateDay(addDays(now(), -2));
	private Date tillDate = getEndOfThisDay(now());

	private List<Registry> registries = list();

	private OrganizationService organizationService;
	private EircRegistryService eircRegistryService;
	private RegistryFPFileTypeService registryFPFileTypeService;
	private String moduleName;
	private FPFileService fileService;

	@NotNull
	public String doExecute() throws Exception {

		List<ObjectFilter> filters = list(
				senderOrganizationFilter,
				recipientOrganizationFilter,
				registryTypeFilter,
				new FPModuleFilter(stub(fileService.getModuleByName(moduleName))),
				new BeginDateFilter(fromDate),
				new EndDateFilter(tillDate)
		);

		registries = eircRegistryService.findObjects(filters, getPager());

		if (log.isDebugEnabled()) {
			log.debug("Total registries found: {}", registries.size());
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

	public void setFromDate(String dt) {
		fromDate = truncateDay(parseDate(dt, currentMonth()));
		log.debug("dt = {}, fromDate = {}", dt, fromDate);
	}

	public void setTillDate(String dt) {
		tillDate = getEndOfThisDay(parseDate(dt, now()));
		log.debug("dt = {}, fromDate = {}", dt, tillDate);
	}

	public Organization getSenderOrg(RegistryProperties properties) {
		EircRegistryProperties props = (EircRegistryProperties) properties;
		return organizationService.readFull(props.getSenderStub());
	}

	public Organization getRecipientOrg(RegistryProperties properties) {
		EircRegistryProperties props = (EircRegistryProperties) properties;
		return organizationService.readFull(props.getRecipientStub());
	}

	public FPFile getRegistryFileInMBFormat(Map<RegistryFPFileType, FPFile> files) {
		if (log.isDebugEnabled()) {
			log.debug("Get registry file in MB format, size = {}", files.size());
		}
		FPFile file = files.get(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT));
		log.debug("Return file: {}", file);
		return file;
	}

	public FPFile getRegistryFileInFPFormat(Map<RegistryFPFileType, FPFile> files) {
		if (log.isDebugEnabled()) {
			log.debug("Get registry file in FP format, size = {}", files.size());
		}
		FPFile file = files.get(registryFPFileTypeService.findByCode(RegistryFPFileType.FP_FORMAT));
		log.debug("Return file: {}", file);
		return file;
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
