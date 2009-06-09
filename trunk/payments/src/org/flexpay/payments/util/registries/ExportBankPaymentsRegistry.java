package org.flexpay.payments.util.registries;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.exception.FlexPayException;
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
import java.util.List;

@Transactional(readOnly = true)
public class ExportBankPaymentsRegistry {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String FILE_ENCODING = "utf8";
	public static final String FILE_NAME = "exportBankPayments.txt";

	private String moduleName;
	private FPFileService fpFileService;
	private RegistryService registryService;
	private RegistryRecordService registryRecordService;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
	public Registry export(@NotNull Registry registry) throws FlexPayException {

		log.info("Start exporting payments registry with id = {}", registry.getId());

		List<RegistryRecord> records = registryRecordService.listRecords(registry);
		log.debug("Found {} records for registry with id = {}", records.size(), registry.getId());

		SimpleDateFormat df = new SimpleDateFormat(RegistryUtil.OPERATION_DATE_FORMAT);

		String userName = SecurityUtil.getUserName();

		FPFile fpFile = new FPFile();
		fpFile.setOriginalName(FILE_NAME);
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

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FPFileUtil.getFileOnServer(fpFile)), FILE_ENCODING), 500);

			for (RegistryRecord record : records) {
				log.debug("Processing record = {}", record);
				StringBuilder sb = new StringBuilder();
				sb.append(RegistryUtil.MESSAGE_TYPE).append(RegistryUtil.FIELD_SEPARATOR).
						append(registry.getId()).append(RegistryUtil.FIELD_SEPARATOR).
						append(record.getServiceCode()).append(RegistryUtil.FIELD_SEPARATOR).
						append(record.getPersonalAccountExt()).append(RegistryUtil.FIELD_SEPARATOR).
						append(record.getCity()).append(RegistryUtil.ADDRESS_SEPARATOR).
						append(record.getStreetType()).append(RegistryUtil.ADDRESS_SEPARATOR).
						append(record.getStreetName()).append(RegistryUtil.ADDRESS_SEPARATOR).
						append(record.getBuildingNum()).append(RegistryUtil.ADDRESS_SEPARATOR).
						append(record.getBuildingBulkNum()).append(RegistryUtil.ADDRESS_SEPARATOR).
						append(record.getApartmentNum()).append(RegistryUtil.FIELD_SEPARATOR).
						append(record.getLastName()).append(RegistryUtil.FIO_SEPARATOR).
						append(record.getFirstName()).append(RegistryUtil.FIO_SEPARATOR).
						append(record.getMiddleName()).append(RegistryUtil.FIELD_SEPARATOR).
						append(df.format(record.getOperationDate())).append(RegistryUtil.FIELD_SEPARATOR).
						append(record.getUniqueOperationNumber()).append(RegistryUtil.FIELD_SEPARATOR).
						append(record.getAmount()).append(RegistryUtil.FIELD_SEPARATOR);

				int i = 1;
				int total = record.getContainers().size();

				for (RegistryRecordContainer container : record.getContainers()) {

					sb.append(container.getData());
					if (i != total) {
						sb.append(RegistryUtil.CONTAINER_SEPARATOR);
					}

					i++;
				}

				log.debug("Write new line to file = {}", sb.toString());

				writer.write(sb.toString());
				writer.newLine();

			}

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
