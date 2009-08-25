package org.flexpay.payments.process.export.job;

public interface GeneratePaymentsRegistryParameterNames {

	String GENERATED_FILE_NAME = "GeneratedFileName";

	String FILE = "File";
	String FILE_ID = "FileId";

	String PAYMENT_POINT_ID = "paymentPointId";

	String ORGANIZATION = "Organization";
	String ORGANIZATION_ID = "OrganizationId";

	String REGISTERED_ORGANIZATION = "RegisteredOrganization";
	String REGISTERED_ORGANIZATION_ID = "RegisteredOrganizationId";

	String SERVICE_PROVIDER = "ServiceProvider";
	String SERVICE_PROVIDER_ID = "ServiceProviderId";

	String REGISTRY = "Registry";
	String REGISTRY_ID = "RegistryId";

	String BEGIN_DATE = "beginDate";
	String END_DATE = "endDate";
	String FINISH_DATE = "finishDate";
	String LAST_PROCESSED_DATE = "lastProcessedDate";

	String EMAIL = "Email";
	
	String PRIVATE_KEY = "PrivateKey";	
}
