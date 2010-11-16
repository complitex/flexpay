package org.flexpay.eirc.actions.registry;

import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.RecordErrorsType;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class RegistryRecordErrorsTypesListAction extends AccountantAWPWithPagerActionSupport<RecordErrorsGroup> {

    private Registry registry = new Registry();
    private List<RecordErrorsType> errorsTypes = list();

    private RegistryRecordService registryRecordService;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        if (registry == null || registry.isNew()) {
            log.warn("Incorrect registry id");
            addActionError(getText("payments.error.registry.incorrect_registry_id"));
            return SUCCESS;
        }

        errorsTypes = registryRecordService.listRecordErrorsTypes(registry);

        if (log.isDebugEnabled()) {
            log.debug("Total errors types found: {}", errorsTypes.size());
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

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public List<RecordErrorsType> getErrorsTypes() {
        return errorsTypes;
    }

    @Required
    public void setRegistryRecordService(RegistryRecordService registryRecordService) {
        this.registryRecordService = registryRecordService;
    }

}
