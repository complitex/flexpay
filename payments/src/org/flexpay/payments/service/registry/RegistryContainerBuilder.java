package org.flexpay.payments.service.registry;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentAddition;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.Service;

public interface RegistryContainerBuilder {

	RegistryContainer getInstanceIdContainer();

	RegistryContainer getServiceSyncContainer(Service service);

	RegistryContainer getRegisterOrganizationSyncContainer(Organization registerOrganization);

	RegistryContainer getProviderOrganizationSyncContainer(ServiceProvider provider);

	RegistryContainer getPaymentPointSyncContainer(PaymentPoint paymentPoint);

	RegistryContainer buildSyncIdContainer(Class<? extends DomainObject> clazz, DomainObject object, String end);

	RegistryRecordContainer getSimplePaymentContainer(Document document);

	RegistryRecordContainer getBankPaymentContainer(Operation operation);

	RegistryRecordContainer getPaymentPointIdContainer(Document document);

	RegistryRecordContainer getExternalOrganizationAccountContainer(DocumentAddition ercAccountAddition);
}
