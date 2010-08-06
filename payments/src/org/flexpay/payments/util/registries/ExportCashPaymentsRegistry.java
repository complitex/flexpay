package org.flexpay.payments.util.registries;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.util.RegistryUtil;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportCashPaymentsRegistry extends RegistryFPFileFormat {

	protected String fileName(@NotNull Registry registry) throws FlexPayException {

		return new SimpleDateFormat(RegistryUtil.EXPORT_FILE_NAME_DATE_FORMAT).format(new Date())
			   + "_" + registry.getSenderCode() + "_" + registry.getId() + "." + RegistryUtil.EXPORT_FILE_EXTENSION + RegistryType.TYPE_CASH_PAYMENTS;

	}
}
