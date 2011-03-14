package org.flexpay.payments.action.registry.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SentRegistryContainer {

    private Long id;
    private Long registryId;
    private String dateFrom;
    private String dateTo;
    private String dateDelivery;
    private String typeRegistry;
    private String recipient;
    private String serviceProvider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegistryId() {
        return registryId;
    }

    public void setRegistryId(Long registryId) {
        this.registryId = registryId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getTypeRegistry() {
        return typeRegistry;
    }

    public void setTypeRegistry(String typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("id", id).
                append("registryId", registryId).
                append("dateFrom", dateFrom).
                append("dateTo", dateTo).
                append("dateDelivery", dateDelivery).
                append("typeRegistry", typeRegistry).
                append("recipient", recipient).
                append("serviceProvider", serviceProvider).
                toString();
    }
}
