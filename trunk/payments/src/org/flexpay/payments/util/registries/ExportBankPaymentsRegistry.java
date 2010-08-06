package org.flexpay.payments.util.registries;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.util.RegistryUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportBankPaymentsRegistry extends RegistryFPFileFormat {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected String fileName(@NotNull Registry registry) throws FlexPayException {

		FetchRange fetchRange = new FetchRange();
		List<RegistryRecord> records = registryRecordService.listRecordsForExport(registry, fetchRange);

		if (fetchRange.getCount() == 0) {
			log.error("Error! Not found records for registry with id {}", registry.getId());
			throw new FlexPayException("Error! Not found records for registry with id " + registry.getId());
		}

		String paymentPointId = "";

		for (RegistryRecordContainer container : registryRecordService.getRecordContainers(records.get(0))) {
			String[] fields = container.getData().split(RegistryUtil.CONTAINER_BODY_SEPARATOR);
			if (RegistryUtil.BANK_PAYMENT_CONTAINER_CODE.equals(fields[0])) {
				paymentPointId = fields[1];
				break;
			}
		}

		return new SimpleDateFormat(RegistryUtil.EXPORT_FILE_NAME_DATE_FORMAT).format(new Date())
			   + "_" + paymentPointId + "_" + registry.getId() + "." + RegistryUtil.EXPORT_FILE_EXTENSION + RegistryType.TYPE_BANK_PAYMENTS;

	}
}
