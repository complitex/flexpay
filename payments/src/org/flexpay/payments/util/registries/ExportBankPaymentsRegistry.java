package org.flexpay.payments.util.registries;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.dao.paging.FetchRange;
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
import org.flexpay.common.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportBankPaymentsRegistry extends RegistryFPFileFormat {

	private Logger log = LoggerFactory.getLogger(getClass());

	private String moduleName;

    protected FPFile generateFile(@NotNull Registry registry) throws FlexPayException {

		FetchRange fetchRange = new FetchRange();
		List<RegistryRecord> records = registryRecordService.listRecordsForExport(registry, fetchRange);

		if (fetchRange.getCount() == 0) {
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

		return fpFileService.create(fpFile);
	}

	@Required
	final public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

    @Required
    final public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }

    @Required
    final public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Required
    final public void setRegistryRecordService(RegistryRecordService registryRecordService) {
        this.registryRecordService = registryRecordService;
    }
}
