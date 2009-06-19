package org.flexpay.payments.util.registries;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.RegistryUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public class ExportBankPaymentsRegistry {

	private Logger log = LoggerFactory.getLogger(getClass());

	private String moduleName;
	private FPFileService fpFileService;
	private RegistryService registryService;
	private RegistryRecordService registryRecordService;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	public Registry export(@NotNull Registry registry) throws FlexPayException {

		log.info("Start exporting payments registry with id = {}", registry.getId());

		List<RegistryRecord> records = registryRecordService.listRecords(registry);
		log.debug("Found {} records for registry with id = {}", records.size(), registry.getId());

		if (records.size() == 0) {
			log.error("Error! Not found records for registry with id {}", registry.getId());
			throw new FlexPayException("Error! Not found records for registry with id " + registry.getId());
		}

		String userName = SecurityUtil.getUserName();
		String paymentPointId = "";

		for (RegistryRecordContainer container : registryRecordService.getRecordContainers(records.get(0))) {
			String[] fields = container.getData().split(RegistryUtil.CONTAINER_BODY_SEPARATOR);
			if (RegistryUtil.BANK_PAYMENT_CONTAINER_CODE.equals(fields[0])) {
				paymentPointId = fields[1];
				break;
			}
		}

		String fileName = new SimpleDateFormat(RegistryUtil.EXPORT_FILE_NAME_DATE_FORMAT).format(new Date())
				+ "_" + paymentPointId + "_" + registry.getId() + "." + RegistryUtil.EXPORT_FILE_EXTENSION;

		FPFile fpFile = new FPFile();
		fpFile.setOriginalName(fileName);
		fpFile.setModule(fpFileService.getModuleByName(moduleName));
		fpFile.setUserName(userName);

		try {
			FPFileUtil.createEmptyFile(fpFile);
		} catch (IOException e) {
			log.error("Can't create export-file for registry with id {}", registry.getId());
			log.error("Error", e);
			return null;
		}

		BufferedWriter writer = null;

		try {

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FPFileUtil.getFileOnServer(fpFile)), RegistryUtil.EXPORT_FILE_ENCODING), 500);

			writer.write(buildHeader(registry));
			writer.newLine();

			for (RegistryRecord record : records) {
				writer.write(buildRecord(registry, record));
				writer.newLine();
			}

			writer.write(buildFooter(registry));
			writer.newLine();

		} catch (IOException e) {
			log.error("Error with writing export-file for registry", e);
			return null;
		} finally {
			IOUtils.closeQuietly(writer);
		}

		fpFile.setSize(FPFileUtil.getFileOnServer(fpFile).length());
		fpFile = fpFileService.create(fpFile);

		registry.setSpFile(fpFile);

		registry = registryService.update(registry);

		log.info("Finish exporting payments registry with id = {}", registry.getId());

		return registry;

	}

	private String buildHeader(Registry registry) {

		StringBuilder header = new StringBuilder();

		log.debug("Building header for registry = {}", registry);

		SimpleDateFormat dfCreation = new SimpleDateFormat(RegistryUtil.REGISTRY_CREATION_DATE_FORMAT);
		SimpleDateFormat dfFrom = new SimpleDateFormat(RegistryUtil.REGISTRY_DATE_FROM_FORMAT);
		SimpleDateFormat dfTill = new SimpleDateFormat(RegistryUtil.REGISTRY_DATE_TILL_FORMAT);

		header.append(RegistryUtil.REGISTY_HEADER_MESSAGE_TYPE_CHAR).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(registry.getId())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(registry.getRegistryType().getCode())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(registry.getRecordsNumber())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(dfCreation.format(registry.getCreationDate()))).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(dfFrom.format(registry.getFromDate()))).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(dfTill.format(registry.getTillDate()))).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(registry.getSenderCode())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(registry.getRecipientCode())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(registry.getAmount())).
				append(RegistryUtil.FIELD_SEPARATOR);

		log.debug("File header = {}", header.toString());

		return header.toString();
	}

	private String buildRecord(Registry registry, RegistryRecord record) {

		log.debug("Building string for record = {}", record);

		SimpleDateFormat df = new SimpleDateFormat(RegistryUtil.OPERATION_DATE_FORMAT);

		StringBuilder sb = new StringBuilder();
		sb.append(RegistryUtil.REGISTRY_RECORD_MESSAGE_TYPE_CHAR).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(registry.getId())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(record.getServiceCode())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(record.getPersonalAccountExt())).
				append(RegistryUtil.FIELD_SEPARATOR).
				//default city is empty
//				append(StringUtil.getString(record.getCity())).
				append(RegistryUtil.ADDRESS_SEPARATOR).
				append(StringUtil.getString(record.getStreetType())).
				append(RegistryUtil.ADDRESS_SEPARATOR).
				append(StringUtil.getString(record.getStreetName())).
				append(RegistryUtil.ADDRESS_SEPARATOR).
				append(StringUtil.getString(record.getBuildingNum())).
				append(RegistryUtil.ADDRESS_SEPARATOR).
				append(StringUtil.getString(record.getBuildingBulkNum())).
				append(RegistryUtil.ADDRESS_SEPARATOR).
				append(StringUtil.getString(record.getApartmentNum())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(record.getLastName())).
				append(RegistryUtil.FIO_SEPARATOR).
				append(StringUtil.getString(record.getFirstName())).
				append(RegistryUtil.FIO_SEPARATOR).
				append(StringUtil.getString(record.getMiddleName())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(df.format(record.getOperationDate()))).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(record.getUniqueOperationNumber())).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(record.getAmount())).
				append(RegistryUtil.FIELD_SEPARATOR);

		int i = 1;
		int total = record.getContainers().size();

		for (RegistryRecordContainer container : record.getContainers()) {

			sb.append(StringUtil.getString(container.getData()));
			if (i != total) {
				sb.append(RegistryUtil.CONTAINER_SEPARATOR);
			}

			i++;
		}

		log.debug("File record = {}", sb.toString());

		return sb.toString();

	}

	private String buildFooter(Registry registry) {

		StringBuilder footer = new StringBuilder();

		log.debug("Building footer for registry = {}", registry);

		footer.append(RegistryUtil.REGISTRY_FOOTER_MESSAGE_TYPE_CHAR).
				append(RegistryUtil.FIELD_SEPARATOR).
				append(StringUtil.getString(registry.getId()));

		log.debug("File footer = {}", footer.toString());

		return footer.toString();
	}

	@Required
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

}
