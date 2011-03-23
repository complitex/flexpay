package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

public class ObjectIdentifierSyncOperation extends ContainerOperation {

	public static final int GLOBAL_IDENTIFIER_TYPE_MASTER_INDEX = 1;

	private int objectType;
	private String senderObjectId;
	private String recipientObjectId;
	private String globalIdentifier;
	private int globalIdentifierType = 0;

	private ServiceOperationsFactory factory;

	public ObjectIdentifierSyncOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {

		super(Integer.parseInt(datum.get(0)));
		this.factory = factory;

		if (datum.size() != 6) {
			throw new InvalidContainerException("Expected 6 fields in Objects Sync container");
		}

		try {
			objectType = Integer.parseInt(datum.get(1));
			senderObjectId = datum.get(2);
			recipientObjectId = datum.get(3);
			globalIdentifier = datum.get(4);
			if (StringUtils.isNotBlank(globalIdentifier)) {
				globalIdentifierType = Integer.parseInt(datum.get(5));
			}
		} catch (Exception ex) {
			throw new InvalidContainerException("Failed parsing container", ex);
		}

		if (StringUtils.isBlank(senderObjectId)) {
			throw new InvalidContainerException("Sender identifier is empty");
		}

		if (StringUtils.isBlank(recipientObjectId) && StringUtils.isBlank(globalIdentifier)) {
			throw new InvalidContainerException("Expected at least recipient identifier or global identifier");
		}

		if (StringUtils.isNotBlank(globalIdentifier) && globalIdentifierType != GLOBAL_IDENTIFIER_TYPE_MASTER_INDEX) {
			throw new InvalidContainerException("Invalid global identifier type: " + globalIdentifierType);
		}
	}

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @return DelayedUpdate object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {

		Class<? extends DomainObject> clazz = getType();
		CorrectionsService correctionsService = factory.getCorrectionsService();
		MasterIndexService indexService = factory.getMasterIndexService();
		EircRegistryProperties props = (EircRegistryProperties) context.getRegistry().getProperties();
		Organization org = factory.getOrganizationService().readFull(stub(props.getSender()));

		// build correction by master index
		if (StringUtils.isNotBlank(globalIdentifier) && globalIdentifierType == GLOBAL_IDENTIFIER_TYPE_MASTER_INDEX) {

			Stub<?> stub = correctionsService.findCorrection(
					globalIdentifier, clazz, indexService.getMasterSourceDescriptionStub());
			if (stub == null) {
				throw new FlexPayException("Cannot find master correction: " + globalIdentifier +
										   " for class " + clazz);
			}

			DomainObject obj = instance(clazz);
			obj.setId(stub.getId());
			DataCorrection correction = correctionsService.getStub(senderObjectId, obj, org.sourceDescriptionStub());
			correctionsService.save(correction);
			return DelayedUpdateNope.INSTANCE;
		}

		// build correction by recipient correction
		if (StringUtils.isNotBlank(recipientObjectId)) {

			Organization recipient = factory.getOrganizationService().readFull(props.getRecipientStub());
			// is recipient self organization? If not try to find correction by recipient data source
			Stub<?> stub = ApplicationConfig.getSelfOrganization().equals(recipient) ?
						   new Stub(Long.parseLong(recipientObjectId)) :
						   correctionsService.findCorrection(
								   recipientObjectId, clazz, recipient.sourceDescriptionStub());
			if (stub == null) {
				throw new FlexPayException("Cannot find object by recipient correction: " + recipientObjectId);
			}

			DomainObject obj = instance(clazz);
			obj.setId(stub.getId());
			DataCorrection correction = correctionsService.getStub(senderObjectId, obj, org.sourceDescriptionStub());
			correctionsService.save(correction);
			return DelayedUpdateNope.INSTANCE;
		}

		return DelayedUpdateNope.INSTANCE;
	}

	private DomainObject instance(Class<? extends DomainObject> clazz) throws FlexPayException {
		try {
			return clazz.newInstance();
		} catch (Exception ex) {
			throw new FlexPayException("Failed creating DomainObject instance of type " + clazz);
		}
	}

	private Class<? extends DomainObject> getType() throws FlexPayException {

		ClassToTypeRegistry typeRegistry = factory.getClassToTypeRegistry();
		if (typeRegistry.getType(Organization.class) == objectType) {
			return Organization.class;
		}
		if (typeRegistry.getType(PaymentPoint.class) == objectType) {
			return PaymentPoint.class;
		}
		if (typeRegistry.getType(Service.class) == objectType) {
			return Service.class;
		}
		if (typeRegistry.getType(ServiceType.class) == objectType) {
			return ServiceType.class;
		}

		throw new FlexPayException("unknown object type: " + objectType);
	}
}
