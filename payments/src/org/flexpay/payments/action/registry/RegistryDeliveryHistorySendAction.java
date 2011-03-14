package org.flexpay.payments.action.registry;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.payments.action.AccountantAWPActionSupport;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.flexpay.payments.process.export.ExportJobParameterNames;
import org.flexpay.payments.service.RegistryDeliveryHistoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.set;

public class RegistryDeliveryHistorySendAction extends AccountantAWPActionSupport {

	private Set<Long> objectIds = set();

    private RegistryDeliveryHistoryService registryDeliveryHistoryService;
    private ProcessManager processManager;

    @NotNull
	@Override
    protected String doExecute() throws Exception {

		if (objectIds == null) {
			log.warn("ObjectIds parameter is null");
			return SUCCESS;
		}

		for (Long historyId : objectIds) {
			RegistryDeliveryHistory history = registryDeliveryHistoryService.read(new Stub<RegistryDeliveryHistory>(historyId));
			if (history == null) {
				log.error("Can't get registry delivery history with id {} from DB", historyId);
				addActionError(getText("payments.error.registry.delivery_history.mail_not_sent_inner_error"));
				continue;
			}

			Map<Serializable, Serializable> parameters = map();
			parameters.put(ExportJobParameterNames.FILE_ID, history.getSpFile().getId());
			parameters.put(ExportJobParameterNames.REGISTRY_ID, history.getRegistry().getId());
			try {
				Long processId = processManager.createProcess("SendRegistry", parameters);
				if (processId == null || processId <= 0) {
					log.error("Incorrect Process id ({})", processId);
					log.error("Process \"SendRegistry\" didn't start for registry delivery history with id {}", historyId);
					addActionError(getText("payments.error.registry.delivery_history.mail_not_sent_inner_error"));
					continue;
				}

				log.debug("Sent e-mail by registry delivery history {} in process {}", historyId, processId);
			} catch (Throwable th) {
				log.error("Exception in delivery message", th);
				addActionError(getText("payments.error.registry.delivery_history.mail_not_sent_inner_error"));
			}
		}

		addActionMessage(getText("payments.registry.delivery_history.mail_sent"));

        return SUCCESS;
    }

    @NotNull
	@Override
    protected String getErrorResult() {
        return SUCCESS;
    }

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Required
    public void setRegistryDeliveryHistoryService(RegistryDeliveryHistoryService registryDeliveryHistoryService) {
        this.registryDeliveryHistoryService = registryDeliveryHistoryService;
    }
}
