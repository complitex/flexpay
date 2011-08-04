package org.flexpay.payments.process.export;

public interface ExportJobParameterNames {

	final static String GENERATED_FILE_NAME = "GeneratedFileName";

	final static String FILE = "file";
	final static String FILE_ID = "fileId";

	final static String PAYMENT_POINT_ID = "paymentPointId";

	final static String ORGANIZATION = "organization";
	final static String ORGANIZATION_ID = "organizationId";

	final static String REGISTERED_ORGANIZATION = "registeredOrganization";
	final static String REGISTERED_ORGANIZATION_ID = "registeredOrganizationId";

	final static String SERVICE_PROVIDER = "serviceProvider";
	final static String SERVICE_PROVIDER_ID = "serviceProviderId";

	final static String REGISTRY = "registry";
	final static String REGISTRY_ID = "registryId";

	final static String BEGIN_DATE = "beginDate";
	final static String END_DATE = "endDate";
	final static String FINISH_DATE = "finishDate";
	final static String LAST_PROCESSED_DATE = "lastProcessedDate";
	final static String EMAIL = "email";
	final static String TRADING_DAY_BEGIN_DATE = "tradingDayBeginDate";
	final static String TRADING_DAY_END_DATE = "tradingDayEndDate";

	final static String PRIVATE_KEY = "privateKey";

	final static String REGISTRIES = "Registries";

	final static String CURRENT_INDEX_PAYMENT_POINT = "currentIndexPaymentPoint";
	final static String PAYMENT_POINTS = "paymentPoints";

	final static String CURRENT_INDEX_CASHBOX = "currentIndexCashbox";
	final static String CASHBOXES = "cashboxes";
	final static String CASHBOX_ID = "cashboxId";

	final static String PAYMENT_COLLECTOR_ID = "paymentCollectorId";
}
