package org.flexpay.payments.service.registry.impl;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.DocumentAddition;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.registry.RegistryContainerBuilder;
import org.flexpay.payments.util.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.util.config.ApplicationConfig.getInstanceId;

public class RegistryContainerBuilderImpl implements RegistryContainerBuilder {

	// delimiter
	private static final String DELIMITER = ":";

	// container types
	private static final String EXTERNAL_ORG_ACCOUNT_CONTAINER_TYPE = "15";
	private static final String SIMPLE_PAYMENT_CONTAINER_TYPE = "50";
	private static final String BANK_PAYMENT_CONTAINER_TYPE = "52";
	private static final String PAYMENT_POINT_ID_CONTAINER_TYPE = "500";
	private static final String INSTANCE_ID_CONTAINER_TYPE = "503";
	private static final String SYNC_ID_CONTAINER_TYPE = "502";

	// predefined field values
	private static final String GLOBAL_FP_ID_TYPE = "1";

	private ClassToTypeRegistry typeRegistry;
	private MasterIndexService masterIndexService;

    @Override
	public RegistryContainer getInstanceIdContainer() {
		return new RegistryContainer(INSTANCE_ID_CONTAINER_TYPE + DELIMITER + getInstanceId());
	}

    @Override
	public RegistryContainer getServiceSyncContainer(Service service) {
		return buildSyncIdContainer(Service.class, service, GLOBAL_FP_ID_TYPE);
	}

    @Override
	public RegistryContainer getRegisterOrganizationSyncContainer(Organization registerOrganization) {
		return buildSyncIdContainer(Organization.class, registerOrganization, GLOBAL_FP_ID_TYPE);
	}

    @Override
	public RegistryContainer getProviderOrganizationSyncContainer(ServiceProvider provider) {
		return buildSyncIdContainer(Organization.class, provider.getOrganization(), GLOBAL_FP_ID_TYPE);
	}

    @Override
	public RegistryContainer getPaymentPointSyncContainer(PaymentPoint paymentPoint) {
		return buildSyncIdContainer(PaymentPoint.class, paymentPoint, GLOBAL_FP_ID_TYPE);
	}

    @Override
	public RegistryContainer buildSyncIdContainer(Class<? extends DomainObject> clazz, DomainObject object, String end) {
		return new RegistryContainer(SYNC_ID_CONTAINER_TYPE + DELIMITER + typeRegistry.getType(clazz) +
									 DELIMITER + object.getId() + DELIMITER +
									 DELIMITER + masterIndexService.getMasterIndex(object) +
									 DELIMITER + end);
	}

    @Override
	public RegistryRecordContainer getSimplePaymentContainer(Document document) {
		return new RegistryRecordContainer(SIMPLE_PAYMENT_CONTAINER_TYPE +
										   DELIMITER + document.getCreditorOrganization().getId());
	}
	
	public RegistryRecordContainer getBankPaymentContainer(Operation operation) {

		return new RegistryRecordContainer(BANK_PAYMENT_CONTAINER_TYPE +
				                           DELIMITER + StringUtil.getString(operation.getRegisterOrganization().getId()) +
										   DELIMITER + StringUtil.getString(operation.getId()) +
				                           DELIMITER + StringUtil.getString(operation.getOperationSum()));
	}

	public RegistryRecordContainer getPaymentPointIdContainer(Document document) {
		return new RegistryRecordContainer(PAYMENT_POINT_ID_CONTAINER_TYPE +
										   DELIMITER + document.getOperation().getPaymentPoint().getId());
	}

    @Override
	public RegistryRecordContainer getExternalOrganizationAccountContainer(DocumentAddition ercAccountAddition) {
		return new RegistryRecordContainer(
				EXTERNAL_ORG_ACCOUNT_CONTAINER_TYPE + DELIMITER +
				"01011900" + DELIMITER + // TODO eliminate constant
				DELIMITER + ercAccountAddition.getStringValue() +
				DELIMITER + ApplicationConfig.getMbOrganizationStub().getId());
	}

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}
}
