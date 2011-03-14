package org.flexpay.payments.action.registry;

import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.action.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.action.registry.data.SentRegistryContainer;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.flexpay.payments.service.RegistryDeliveryHistoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.DateUtil.getEndOfThisDay;
import static org.flexpay.common.util.DateUtil.truncateDay;

public class RegistryDeliveryHistoryAction extends AccountantAWPWithPagerActionSupport<RegistryDeliveryHistory> {

    private static final String TIME_FORMAT = "HH:mm";

	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private List<SentRegistryContainer> deliveryHistory;

    private RegistryDeliveryHistoryService registryDeliveryHistoryService;
    private OrganizationService organizationService;
    private ServiceProviderService providerService;

    @NotNull
	@Override
    protected String doExecute() throws Exception {

        deliveryHistory = list();

		final Date begin = truncateDay(beginDateFilter.getDate());
		final Date end = getEndOfThisDay(endDateFilter.getDate());
		log.debug("Getting delivery history in date range: {} - {}", begin, end);

        List<RegistryDeliveryHistory> histories = registryDeliveryHistoryService.listRegistryDeliveryHistories(getPager(), begin, end);
        log.debug("Found {} registry delivery histories", histories.size());

        for (RegistryDeliveryHistory history : histories) {
            SentRegistryContainer container = new SentRegistryContainer();
            container.setId(history.getId());
            container.setRegistryId(history.getRegistry().getId());
            container.setDateFrom(format(history.getRegistry().getFromDate()));
            container.setDateTo(DateUtil.format(history.getRegistry().getTillDate()));
            container.setDateDelivery(DateUtil.format(history.getDeliveryDate()) + " " + new SimpleDateFormat(TIME_FORMAT).format(history.getDeliveryDate()));
            container.setTypeRegistry(getText(history.getRegistry().getRegistryType().getI18nName()));

            EircRegistryProperties prop = (EircRegistryProperties)history.getRegistry().getProperties();
            Organization recipient = organizationService.readFull(prop.getRecipientStub());
            ServiceProvider provider = providerService.read(prop.getServiceProviderStub());

            if (recipient != null) {
                container.setRecipient(recipient.getName(getLocale()));
            } else {
                log.warn("Recipient does not content in registry properties {}", history.getRegistry().getId());
            }
            if (provider != null) {
				Translation translation = TranslationUtil.getTranslation(provider.getDescriptions(), getLocale());
                container.setServiceProvider(translation != null ? translation.getName() : "Translation not defined");
            } else {
                log.warn("Service provider does not content in properties {}", history.getRegistry().getId());
            }

            log.debug("Addding container with history {}", history.getId());
            deliveryHistory.add(container);
        }

        return SUCCESS;
    }

    @NotNull
	@Override
    protected String getErrorResult() {
        return SUCCESS;
    }

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public void setEndDateFilter(EndDateFilter endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

    public List<SentRegistryContainer> getDeliveryHistory() {
        return deliveryHistory;
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
    public void setRegistryDeliveryHistoryService(RegistryDeliveryHistoryService registryDeliveryHistoryService) {
        this.registryDeliveryHistoryService = registryDeliveryHistoryService;
    }
}
