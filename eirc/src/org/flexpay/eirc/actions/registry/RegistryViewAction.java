package org.flexpay.eirc.actions.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.ServiceTypeNameTranslation;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.filters.ImportErrorTypeFilter;
import org.flexpay.eirc.persistence.filters.RegistryRecordStatusFilter;
import org.flexpay.eirc.service.ServiceTypeService;
import org.flexpay.eirc.service.RegistryRecordService;
import org.flexpay.eirc.service.RegistryService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RegistryViewAction extends FPActionSupport {

	private RegistryService registryService;
	private ServiceTypeService serviceTypeService;
	private RegistryRecordService registryRecordService;
	private ClassToTypeRegistry classToTypeRegistry;

	private SpRegistry registry = new SpRegistry();
	private List<RegistryRecord> records = Collections.emptyList();
	private Page<RegistryRecord> pager = new Page<RegistryRecord>();

	private ImportErrorTypeFilter importErrorTypeFilter = new ImportErrorTypeFilter();
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();

	@NotNull
	public String doExecute() throws Exception {
		if (registry.getId() == null) {
			addActionError("No registryId specified, give up.");
			return ERROR;
		}
		importErrorTypeFilter.init(classToTypeRegistry);
		registry = registryService.read(stub(registry));
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
		return REDIRECT_ERROR;
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

	public Page<RegistryRecord> getPager() {
		return pager;
	}

	public void setPager(Page<RegistryRecord> pager) {
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

	public List<RegistryRecord> getRecords() {
		return records;
	}

	public void setRecords(List<RegistryRecord> records) {
		this.records = records;
	}

	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}
}
