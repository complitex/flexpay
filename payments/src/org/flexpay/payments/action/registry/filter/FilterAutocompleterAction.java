package org.flexpay.payments.action.registry.filter;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.FilterObject;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.filter.FilterData;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.flexpay.common.persistence.filter.StringValueFilter.TYPE_APARTMENT;
import static org.flexpay.common.persistence.filter.StringValueFilter.TYPE_BUILDING;
import static org.flexpay.common.util.CollectionUtils.list;

public class FilterAutocompleterAction extends AccountantAWPActionSupport {

    private Registry registry;
    private String q;
    private FilterData filterData = new FilterData();
    protected List<FilterObject> foundObjects = list();

    private RegistryRecordService registryRecordService;

    @NotNull
	@Override
    protected String doExecute() throws Exception {

        if (registry == null || registry.isNew()) {
            log.warn("Incorrect registry id");
            addActionError(getText("payments.error.registry.incorrect_registry_id"));
            return SUCCESS;
        }

        if (filterData == null || filterData.hasIncorrectType()) {
            log.warn("Incorrect filter data {}", filterData);
            return SUCCESS;
        }

        if (q == null) {
            q = "";
        }

        Page<String> pager = new Page<String>(10);
        if (filterData.getType().equals(TYPE_BUILDING) || filterData.getType().equals(TYPE_APARTMENT)) {
            pager = new Page<String>(10000);
            if (isNotEmpty(q)) {
                filterData.setString(q + "%");
            }
        } else {
            if (isNotEmpty(q)) {
                filterData.setString("%" + q + "%");
            }
        }

        List<String> result = registryRecordService.listAutocompleterAddresses(registry, filterData, pager);
        if (log.isDebugEnabled()) {
            log.debug("Found {} strings in registry with id = {}, and query = {}, for type {}", new Object[] {result.size(), registry.getId(), filterData.getString(), filterData.getType()});
        }

        for (String name : result) {
            if (name == null) {
                log.warn("Found incorrect object: {}", name);
                continue;
            }
            foundObjects.add(new FilterObject("", name));
        }

        return SUCCESS;
    }

    @NotNull
	@Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public List<FilterObject> getFoundObjects() {
        return foundObjects;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void setFilterData(FilterData filterData) {
        this.filterData = filterData;
    }

    public void setQ(String q) {
        this.q = q;
    }

    @Required
    public void setRegistryRecordService(RegistryRecordService registryRecordService) {
        this.registryRecordService = registryRecordService;
    }
}
