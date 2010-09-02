package org.flexpay.payments.util.registries;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.RegistryUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.io.WriterCallback;
import org.flexpay.payments.service.SignatureService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.io.Writer;
import java.security.Signature;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Generate file in FP format.
 * <br/>
 * Content basic logic and similar behaviour.
 */
public class RegistryFPFileFormat {

	private Logger log = LoggerFactory.getLogger(getClass());

	private String moduleName;

	protected FPFileService fpFileService;
	protected RegistryService registryService;
	protected RegistryRecordService registryRecordService;
    protected RegistryFPFileTypeService registryFPFileTypeService;
	private SignatureService signatureService;

	public FPFile generateAndAttachFile(@NotNull Registry registry) throws FlexPayException {
		return generateAndAttachFile(registry, null);
	}

	public FPFile generateAndAttachFile(@NotNull Registry registry, @Nullable String privateKey) throws FlexPayException {

		log.info("Start generating FPfile for registry #{}", registry.getId());

        RegistryFPFileType fpFileType = registryFPFileTypeService.findByCode(RegistryFPFileType.FP_FORMAT);
        if (fpFileType == null) {
            log.error("Did not find registry FP file type");
            return null;
        }

		StopWatch watch = new StopWatch();
		watch.start();
		FPFile result = export(registry, createFile(registry), privateKey);

        FPFile fpFile = fpFileService.update(result);
        registry.getFiles().put(fpFileType, fpFile);

		registryService.update(registry);

		log.info("Finished dumping registry #{}, time spent {}", registry.getId(), watch);
		return result;
	}

	protected String fileName(Registry registry) throws FlexPayException {
		return "ree_" + registry.getId() + ".ree_" + registry.getRegistryType().getCode();
	}

	protected FPFile createFile(@NotNull Registry registry) throws FlexPayException {

		FPFile fpFile = new FPFile();
		String userName = SecurityUtil.getUserName();
		fpFile.setOriginalName(fileName(registry));
		fpFile.setModule(fpFileService.getModuleByName(moduleName));
		fpFile.setUserName(userName);

		try {
			FPFileUtil.createEmptyFile(fpFile);
		} catch (IOException e) {
			log.error("Can't create export-file for registry #" + registry.getId(), e);
			return null;
		}

		return fpFileService.create(fpFile);
	}

	protected FPFile export(@NotNull final Registry registry, FPFile fpFile) throws FlexPayException {
		return export(registry, fpFile, null);
	}

	protected FPFile export(@NotNull final Registry registry, FPFile fpFile, String privateKey) throws FlexPayException {
		final Signature privateSignature = getPrivateSignature(privateKey);

		try {
			fpFile.withWriter(RegistryUtil.EXPORT_FILE_ENCODING, new WriterCallback() {
				public void write(Writer w) throws IOException {
					FetchRange fetchRange = new FetchRange();
					List<RegistryRecord> records = registryRecordService.listRecordsForExport(registry, fetchRange);

					writeLine(w, buildHeader(registryService.readWithContainers(Stub.stub(registry))));

					do {
						for (RegistryRecord record : records) {
							writeLine(w, buildRecord(registry, record));
						}
						fetchRange.nextPage();
					} while (!(records = registryRecordService.listRecordsForExport(registry, fetchRange)).isEmpty());

					w.write(buildFooter(registry, privateSignature));
					w.write('\n');
				}

				private void writeLine(@NotNull Writer w, @NotNull String line) throws IOException {
					if (privateSignature != null) {
						try {
							privateSignature.update(line.getBytes());
							privateSignature.update("\n".getBytes());
						} catch (SignatureException e) {
							throw new IOException("Can not update signature", e);
						}
					}
					w.write(line);
					w.write('\n');
				}
			});

			return  fpFile;
		} catch (IOException e) {
			log.error("Error writing export-file for registry", e);
		}
		return null;
	}

	private Signature getPrivateSignature(String privateKey) throws FlexPayException {
		if (privateKey != null) {
			try {
				return signatureService.readPrivateSignature(privateKey);
			} catch (Exception e) {
				throw new FlexPayException("Error read private signature: " + privateKey, e);
			}
		}
		return null;
	}

	protected String buildHeader(Registry registry) {

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
				append(StringUtil.getString(registry.getAmount()));
		List<RegistryContainer> containers = registry.getContainers();
		if (!containers.isEmpty()) {
			header.append(RegistryUtil.FIELD_SEPARATOR);
			boolean first = true;
			for (RegistryContainer container : containers) {
				if (!first) {
					header.append(RegistryUtil.CONTAINER_SEPARATOR);
				}
				header.append(container.getData());
				first = false;
			}
		} else {
			if (registry.getRegistryType().getCode() == RegistryType.TYPE_BANK_PAYMENTS) {
				header.append(RegistryUtil.FIELD_SEPARATOR);
			}
		}

		log.debug("File header = {}", header.toString());
		return header.toString();
	}

	protected String buildRecord(Registry registry, RegistryRecord record) {

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
				//default town is empty
//				append(StringUtil.getString(record.getTownName())).
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

	protected String buildFooter(Registry registry, Signature privateSignature) throws IOException {

		StringBuilder footer = new StringBuilder();

		log.debug("Building footer for registry = {}", registry);

		try {
			footer.append(RegistryUtil.REGISTRY_FOOTER_MESSAGE_TYPE_CHAR).
					append(RegistryUtil.FIELD_SEPARATOR).
					append(StringUtil.getString(registry.getId())).
					append(RegistryUtil.FIELD_SEPARATOR);
			if (privateSignature != null) {
				footer.append(new String(privateSignature.sign(), RegistryUtil.EXPORT_FILE_ENCODING));
			}
		} catch (SignatureException e) {
			throw new IOException("Can not create digital signature", e);
		}

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

    @Required
    public void setRegistryFPFileTypeService(RegistryFPFileTypeService registryFPFileTypeService) {
        this.registryFPFileTypeService = registryFPFileTypeService;
    }

	@Required
	public void setSignatureService(SignatureService signatureService) {
		this.signatureService = signatureService;
	}
}
