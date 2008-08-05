package org.flexpay.eirc.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.ServiceTypeNameTranslation;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.flexpay.eirc.service.ServiceTypeService;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ShowRegistryAction extends FPActionSupport {

	private SpRegistryService registryService;
	private ServiceTypeService serviceTypeService;
	private SpRegistryRecordService registryRecordService;
	private ClassToTypeRegistry classToTypeRegistry;

	private SpRegistry registry = new SpRegistry();
	private List<SpRegistryRecord> records = Collections.emptyList();
	private Page<SpRegistryRecord> pager = new Page<SpRegistryRecord>();

	private ImportErrorTypeFilter importErrorTypeFilter = new ImportErrorTypeFilter();
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();

	@NotNull
	public String doExecute() throws Exception {
		if (registry.getId() == null) {
			addActionError("No registryId specified, give up.");
			return ERROR;
		}
		importErrorTypeFilter.init(classToTypeRegistry);
		registry = registryService.read(registry.getId());
		records = registryRecordService.listRecords(registry, importErrorTypeFilter, recordStatusFilter, pager);

		if (log.isInfoEnabled()) {
			log.info(String.format("pager: size %d, total %d, first %d",
					pager.getPageSize(), pager.getTotalNumberOfElements(), pager.getThisPageFirstElementNumber()));
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
	protected String getErrorResult() {
		return ERROR;
	}

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = serviceTypeService.getServiceType(typeStub);
		ServiceTypeNameTranslation name = getTranslation(type.getTypeNames());
		return name == null ? "Unknown" : name.getName();
	}

	public SpRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(SpRegistry registry) {
		this.registry = registry;
	}

	public Page<SpRegistryRecord> getPager() {
		return pager;
	}

	public void setPager(Page<SpRegistryRecord> pager) {
		this.pager = pager;
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

	public List<SpRegistryRecord> getRecords() {
		return records;
	}

	public void setRecords(List<SpRegistryRecord> records) {
		this.records = records;
	}

	public void setRegistryService(SpRegistryService registryService) {
		this.registryService = registryService;
	}

	public void setRegistryRecordService(SpRegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}
}
