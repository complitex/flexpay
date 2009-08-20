package org.flexpay.payments.actions.registry;

import org.flexpay.payments.actions.CashboxCookieWithPagerActionSupport;
import org.flexpay.payments.actions.registry.data.SentRegistryContainer;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.service.RegistryDeliveryHistoryService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;
import java.io.Serializable;

public class RegistryDeliveryHistoryAction extends CashboxCookieWithPagerActionSupport<RegistryDeliveryHistory> {

    // date filters
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();

    // submit flags
    private String filterSubmitted;

    private RegistryDeliveryHistoryService registryDeliveryHistoryService;
    private OrganizationService organizationService;
    private ServiceProviderService providerService;

    private ProcessManager processManager;

    private Set<Long> objectIds = new HashSet<Long>();
    private List<SentRegistryContainer> deliveryHistory = new ArrayList<SentRegistryContainer>();
    private Date beginDate = null;
    private Date endDate = null;

    @NotNull
    protected String doExecute() throws Exception {
        if (beginDate == null && endDate == null) {
            initFiltersWithDefaults();
        } else if (isFilterSubmitted()) {
			if (!doFilterValidation()) {
				return ERROR;
			}
		} else {
            beginDateFilter.setDate(beginDate);
            endDateFilter.setDate(endDate);
        }

        if (submitted != null && objectIds != null && objectIds.size() > 0) {
            for (Long historyId : objectIds) {
                RegistryDeliveryHistory history = registryDeliveryHistoryService.read(new Stub<RegistryDeliveryHistory>(historyId));
                if (history != null) {
                    Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
                    parameters.put("FileId", history.getSpFile().getId());
                    parameters.put("RegistryId", history.getRegistry().getId());
                    try {
                        Long processId = processManager.createProcess("SendRegisry", parameters);
                        if (processId != null && processId > 0) {
                            log.debug("Sent e-mail by registry delivery history {} in process {}", new Object[]{historyId, processId});
                            addActionMessage(getText("payments.registry.delivery_history.mail_sent"));
                        } else {
                            log.error("Process \"SendRegistry\" did not start for registry delivery history {}");
                            addActionError(getText("payments.error.registry.delivery_history.mail_not_sent_inner_error"));
                        }
                    } catch (Throwable th) {
                        log.error("Exception in delivery message", th);
                        addActionError(getText("payments.error.registry.delivery_history.mail_not_sent_inner_error"));
                    }
                } else {
                    log.error("Registry delivery history {} did not find. Mail did not send.", historyId);
                    addActionError(getText("payments.error.registry.delivery_history.mail_not_sent_inner_error"));
                }
            }
        }

        deliveryHistory.clear();
        List<RegistryDeliveryHistory> histories = loadRegistryDeliveryHistories();
        log.debug("count registry delivery history: {}", histories.size());
        for (RegistryDeliveryHistory history : histories) {
            SentRegistryContainer container = new SentRegistryContainer();
            container.setId(history.getId());
            container.setRegistryId(history.getRegistry().getId());
            container.setDateFrom(DateUtil.format(history.getRegistry().getFromDate()));
            container.setDateTo(DateUtil.format(history.getRegistry().getTillDate()));
            container.setDateDelivery(DateUtil.format(history.getDeliveryDate()));
            container.setTypeRegistry(history.getRegistry().getRegistryType().getI18nName());

            EircRegistryProperties prop = (EircRegistryProperties)history.getRegistry().getProperties();
            Organization recipient = organizationService.readFull(prop.getRecipientStub());
            ServiceProvider provider = providerService.read(prop.getServiceProviderStub());

            if (recipient != null) {
                container.setRecipient(recipient.getName(getLocale()));
            } else {
                log.warn("Recipient does not content in registry properties {}", history.getRegistry().getId());
            }
            if (provider != null) {
                container.setServiceProvider(provider.getName(getLocale()));
            } else {
                log.warn("Service provider does not content in properties {}", history.getRegistry().getId());
            }
            log.debug("add container with history {}", history.getId());
            deliveryHistory.add(container);
        }

        return SUCCESS;
    }

    @NotNull
    protected String getErrorResult() {
        return SUCCESS;
    }

    private boolean isFilterSubmitted() {
		return filterSubmitted != null;
	}

    public void setFilterSubmitted(String filterSubmitted) {
		this.filterSubmitted = filterSubmitted;
	}

    // form data
	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

    public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

    public String getBeginDate() {
        return DateUtil.format(beginDate);
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = DateUtil.parseBeginDate(beginDate);
    }

    public String getEndDate() {
        return DateUtil.format(endDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = DateUtil.parseBeginDate(endDate);
    }

    public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

    public List<SentRegistryContainer> getDeliveryHistory() {
        return deliveryHistory;
    }

    // load history according to search criteria
	private List<RegistryDeliveryHistory> loadRegistryDeliveryHistories() {
        final Date begin = beginDateFilter.getDate();
        final Date end = DateUtil.getEndOfThisDay(endDateFilter.getDate());
        log.debug("get delivery history in date range: {} - {}", new Object[] {begin, end});

        return registryDeliveryHistoryService.listRegistryDeliveryHistories(getPager(), begin, end);
	}

    private boolean doFilterValidation() {

		// dates validation
		beginDate = beginDateFilter.getDate();
		endDate = endDateFilter.getDate();

		if (beginDate.after(endDate)) {
			addActionError(getText("payments.error.registry.delivery.history.begin_date_must_be_before_end_date"));
		}

		return !hasActionErrors();
	}

	private void initFiltersWithDefaults() {
        beginDate = DateUtil.getEndOfThisDay(new Date(0));
        endDate = DateUtil.now();

		beginDateFilter.setDate(beginDate);
		endDateFilter.setDate(endDate);
	}

    @Required
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Required
    public void setProviderService(ServiceProviderService providerService) {
        this.providerService = providerService;
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
