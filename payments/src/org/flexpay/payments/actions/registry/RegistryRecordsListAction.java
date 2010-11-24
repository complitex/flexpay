package org.flexpay.payments.actions.registry;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.persistence.registry.filter.StringFilter;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandlerFactory;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.common.persistence.registry.RegistryRecordStatus.PROCESSED_WITH_ERROR;
import static org.flexpay.common.persistence.registry.filter.StringFilter.*;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;


public class RegistryRecordsListAction extends AccountantAWPWithPagerActionSupport<RegistryRecord> {

	private Registry registry = new Registry();
	private List<RegistryRecord> records = list();
    private Map<Integer, ServiceType> types = map();

    private StringFilter townFilter = new StringFilter();
    private StringFilter streetFilter = new StringFilter();
    private StringFilter buildingFilter = new StringFilter();
    private StringFilter apartmentFilter = new StringFilter();
    private StringFilter fioFilter = new StringFilter();

	protected ImportErrorTypeFilter importErrorTypeFilter = null;
	private RegistryRecordStatusFilter recordStatusFilter = new RegistryRecordStatusFilter();

	private ServiceTypeService serviceTypeService;
	private RegistryRecordService registryRecordService;
	private ClassToTypeRegistry classToTypeRegistry;

	private ProcessingReadHintsHandlerFactory hintsHandlerFactory = null;

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

        List<ObjectFilter> filters = initFilters();

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Import error type filter init: {}", watch);
			watch.reset();
			watch.start();
		}

		records = registryRecordService.listRecords(registry, filters, getPager());

		if (hintsHandlerFactory != null && records.size() > 0) {
			log.debug("select consumers with eirc account for registry records");
			hintsHandlerFactory.getInstance(null, null, records).read();
		}

		if (log.isDebugEnabled()) {
			watch.stop();
			log.debug("Time spent listing records: {}", watch);
			log.debug("Total records found: {}", records.size());
		}

        if (types == null) {
            types = map();
        }

/*
        Set<Integer> typeCodes = set();

        for (RegistryRecord record : records) {
            typeCodes.add(Integer.parseInt(record.getServiceCode()));
        }

        List<ServiceType> typesList = serviceTypeService.getByCodes(typeCodes);
        for (ServiceType type : typesList) {
            types.put(type.getCode(), type);
        }
*/


		return SUCCESS;
	}

    private List<ObjectFilter> initFilters() {

        List<ObjectFilter> filters = list();

        filters.add(recordStatusFilter);

        if (importErrorTypeFilter == null) {
            importErrorTypeFilter = new ImportErrorTypeFilter();
        }

        importErrorTypeFilter.init(classToTypeRegistry);

        filters.add(importErrorTypeFilter);

        if (isNotEmpty(townFilter.getValue())) {
            townFilter.setType(TYPE_TOWN);
            townFilter.setValue("%" + townFilter.getValue() + "%");
            filters.add(townFilter);
        }
        if (isNotEmpty(streetFilter.getValue())) {
            streetFilter.setType(TYPE_STREET);
            streetFilter.setValue("%" + streetFilter.getValue() + "%");
            filters.add(streetFilter);
        }
        if (isNotEmpty(buildingFilter.getValue())) {
            buildingFilter.setType(TYPE_BUILDING);
            buildingFilter.setValue(buildingFilter.getValue() + "%");
            filters.add(buildingFilter);
        }
        if (isNotEmpty(apartmentFilter.getValue())) {
            apartmentFilter.setType(TYPE_APARTMENT);
            filters.add(apartmentFilter);
        }
        if (isNotEmpty(fioFilter.getValue())) {
            fioFilter.setType(TYPE_FIO);
            fioFilter.setValue("%" + fioFilter.getValue() + "%");
            filters.add(fioFilter);
        }

        return filters;

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

	public String getServiceTypeName(String typeCode) throws FlexPayException {
		return getTranslationName(types.get(Integer.parseInt(typeCode)).getTypeNames());
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

    public void setTownFilter(StringFilter townFilter) {
        this.townFilter = townFilter;
    }

    public void setStreetFilter(StringFilter streetFilter) {
        this.streetFilter = streetFilter;
    }

    public void setBuildingFilter(StringFilter buildingFilter) {
        this.buildingFilter = buildingFilter;
    }

    public void setApartmentFilter(StringFilter apartmentFilter) {
        this.apartmentFilter = apartmentFilter;
    }

    public void setFioFilter(StringFilter fioFilter) {
        this.fioFilter = fioFilter;
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

	public void setHintsHandlerFactory(ProcessingReadHintsHandlerFactory hintsHandlerFactory) {
		this.hintsHandlerFactory = hintsHandlerFactory;
	}
}
