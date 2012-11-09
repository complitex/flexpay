package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.RegistryType;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Required;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.GENERATED_FILE_NAME;
import static org.flexpay.payments.process.export.ExportJobParameterNames.ORGANIZATION_ID;

public class GeneratePaymentRegistryFileNameHandler extends ProcessInstanceExecuteHandler {

	private RegistryService registryService;

	//private long TIME_OUT = 2000;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		String fileName = generatePaymentsRegistryFileName(parameters);
		/*
		log.debug("Sleep process {} (Thread id={}, time out={})", new Object[]{getProcessInstanceId(parameters), Thread.currentThread().getId(), TIME_OUT});
		try {
			Thread.sleep(TIME_OUT);
		} catch (InterruptedException e) {
			log.error("Interrupted", e);
		}
		TIME_OUT += 300;
		log.debug("Wake up process {}", getProcessInstanceId(parameters));
		*/
		if (fileName == null) {
			log.error("Registry file name generation failed");
			return RESULT_ERROR;
		}

		parameters.put(GENERATED_FILE_NAME, fileName);

		return RESULT_NEXT;
	}

	private String generatePaymentsRegistryFileName(Map<String, Object> parameters) {

		Date now = new Date();
		StringBuilder result = new StringBuilder();

		result.append(getOrganizationId(parameters));
		result.append(getSerialNumber(parameters));
		result.append("00.");
		result.append(getYearSymbol(now));
		result.append(getMonthSymbol(now));
		result.append(getDaySymbol(now));

		return result.toString();
	}

	private String getOrganizationId(Map<String, Object> parameters) {

		Long paramValue = (Long) parameters.get(ORGANIZATION_ID);
		if (paramValue == null) {
			log.error("Organization id was not found as a job parameter");
			return null;
		}

		String strValue = paramValue.toString();

		if (strValue.length() == 5) {

			return strValue;
		} else if (strValue.length() < 5) {

			int numberOfZeros = 5 - strValue.length();
			StringBuilder result = new StringBuilder();

			for (int i = 0; i < numberOfZeros; i++) {
				result.append("0");
			}
			result.append(paramValue);

			return result.toString();
		} else {

			log.warn("Organization id is too long: {}", paramValue);
			return strValue;
		}
	}

	private String getSerialNumber(Map<String, Object> parameters) {

		Long organizationId = (Long) parameters.get(ORGANIZATION_ID);

		Date now = new Date();
		Date dayStart = DateUtil.truncateDay(now);
		Date dayEnd = DateUtil.getEndOfThisDay(now);

		Long regCount = registryService.getRegistriesCount(RegistryType.TYPE_CASH_PAYMENTS, organizationId, dayStart, dayEnd);
		regCount++;

		return regCount.toString();
	}

	private String getYearSymbol(Date date) {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		Integer digit = year % 10;

		return digit.toString();
	}

	private String getMonthSymbol(Date date) {

		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		return Integer.toString(month, 12).toUpperCase();
	}

	private String getDaySymbol(Date date) {

		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		return Integer.toString(day, 31).toUpperCase();
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}
}
