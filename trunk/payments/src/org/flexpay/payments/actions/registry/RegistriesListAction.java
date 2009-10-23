package org.flexpay.payments.actions.registry;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.*;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.persistence.registry.RegistryProperties;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.common.service.RegistryTypeService;
import static org.flexpay.common.util.CollectionUtils.list;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
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

public class RegistriesListAction extends FPActionWithPagerSupport {

	private OrganizationFilter senderOrganizationFilter = new SenderOrganizationFilter();
	private OrganizationFilter recipientOrganizationFilter = new RecipientOrganizationFilter();
	private RegistryTypeFilter registryTypeFilter = new RegistryTypeFilter();
	private Date fromDate = DateUtils.addDays(DateUtil.now(), -2);
	private Date tillDate = new Date();

	private List<Registry> registries;

	private OrganizationService organizationService;
	private EircRegistryService eircRegistryService;
	private RegistryTypeService registryTypeService;
	private RegistryFPFileTypeService registryFPFileTypeService;

	private String moduleName;
	private FPFileService fileService;

	@NotNull
	public String doExecute() throws Exception {

		StopWatch watch = new StopWatch();
		watch.start();
		senderOrganizationFilter.setOrganizations(organizationService.listOrganizations());
		watch.stop();
		log.debug("Time spent initializing sender filter: {}", watch);
		watch.reset();
		watch.start();
		recipientOrganizationFilter.setOrganizations(organizationService.listOrganizations());
		watch.stop();
		log.debug("Time spent initializing recipient filter: {}", watch);

		registryTypeService.initFilter(registryTypeFilter);

		watch.reset();
		watch.start();

		fromDate = DateUtil.truncateDay(fromDate);
		tillDate = DateUtil.getEndOfThisDay(tillDate);

		List<ObjectFilter> filters = list(
				senderOrganizationFilter,
				recipientOrganizationFilter,
				registryTypeFilter,
				new FPModuleFilter(stub(fileService.getModuleByName(moduleName))),
				new BeginDateFilter(fromDate),
				new EndDateFilter(tillDate)
		);

		registries = eircRegistryService.findObjects(filters, getPager());
		watch.stop();
		log.debug("Time spent listing registries: {}", watch);
		log.debug("Total registries found: {}", registries.size());

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

	public void setRegistries(List<Registry> registries) {
		this.registries = registries;
	}

	public OrganizationFilter getSenderOrganizationFilter() {
		return senderOrganizationFilter;
	}

	public void setSenderOrganizationFilter(OrganizationFilter senderOrganizationFilter) {
		this.senderOrganizationFilter = senderOrganizationFilter;
	}

	public OrganizationFilter getRecipientOrganizationFilter() {
		return recipientOrganizationFilter;
	}

	public void setRecipientOrganizationFilter(OrganizationFilter recipientOrganizationFilter) {
		this.recipientOrganizationFilter = recipientOrganizationFilter;
	}

	public RegistryTypeFilter getRegistryTypeFilter() {
		return registryTypeFilter;
	}

	public void setRegistryTypeFilter(RegistryTypeFilter registryTypeFilter) {
		this.registryTypeFilter = registryTypeFilter;
	}

	public String getFromDate() {
		return format(fromDate);
	}

	public void setFromDate(String dt) {
		fromDate = DateUtil.parseDate(dt, DateUtil.currentMonth());
		log.debug("dt = {}, fromDate = {}", dt, fromDate);
	}

	public String getTillDate() {
		return format(tillDate);
	}

	public void setTillDate(String dt) {
		tillDate = DateUtil.parseDate(dt, DateUtil.now());
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
		log.debug("get registry file in MB format, size={}", files.size());
		FPFile file = files.get(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT));
		log.debug("return file: {}", file);
		return file;
	}

	public FPFile getRegistryFileInFPFormat(Map<RegistryFPFileType, FPFile> files) {
		log.debug("get registry file in FP format, size={}", files.size());
		FPFile file = files.get(registryFPFileTypeService.findByCode(RegistryFPFileType.FP_FORMAT));
		log.debug("return file: {}", file);
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
	public void setRegistryTypeService(RegistryTypeService registryTypeService) {
		this.registryTypeService = registryTypeService;
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
