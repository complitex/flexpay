package org.flexpay.eirc.persistence.exchange;

import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.AccountRecord;
import org.flexpay.eirc.persistence.AccountRecordType;
import org.flexpay.eirc.service.AccountRecordService;
import org.flexpay.eirc.service.ReportPeriodService;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceOperation extends Operation {

	private static Logger log = Logger.getLogger(BalanceOperation.class);

	private ServiceOperationsFactory factory;

	public BalanceOperation(ServiceOperationsFactory factory) {
		this.factory = factory;
	}

	/**
	 * Setup Consumer balance
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws FlexPayException if failure occurs
	 */
	public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {

		ReportPeriodService reportPeriodService = factory.getReportPeriodService();
		AccountRecordService recordService = factory.getAccountRecordService();
		SPService spService = factory.getSpService();

		// if registry is for open report period - just modify balance
		if (reportPeriodService.isInOpenPeriod(registry.getFromDate())) {
			BigDecimal balance = recordService.getCurrentBalance(record.getConsumer());
			if (balance.equals(record.getAmount())) {
				log.info("Balance OK for consumer #" + record.getConsumer().getId());
				return;
			}

			BigDecimal balanceCorrection = balance.subtract(record.getAmount());
			AccountRecord newRecord = new AccountRecord();
			newRecord.setAmount(balanceCorrection);
			newRecord.setConsumer(record.getConsumer());
			newRecord.setOrganisation(ApplicationConfig.getInstance().getSelfOrganisation());
			newRecord.setOperationDate(new Date());
			newRecord.setRecordType(spService.getRecordType(AccountRecordType.TYPE_UNKNOWN));

			recordService.create(newRecord);
		}

		
	}
}
