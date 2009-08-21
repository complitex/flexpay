package org.flexpay.payments.util.registries;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.RegistryUtil;
import org.flexpay.common.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class RegistryFPFileFormat {
    private Logger log = LoggerFactory.getLogger(getClass());

    protected FPFileService fpFileService;
    protected RegistryService registryService;
    protected RegistryRecordService registryRecordService;

    final public Registry generateAndAttachFile(@NotNull Registry registry) throws FlexPayException {
        log.info("Start generate and attach file in flexpay format to registry with id = {}", registry.getId());

        Registry result = export(registry, generateFile(registry));

        log.info("Finish generating and attaching file in flexpay format to registry with id = {}", registry.getId());
        return result;
    }

    protected abstract FPFile generateFile(@NotNull Registry registry) throws FlexPayException;

    protected Registry export(@NotNull Registry registry, FPFile fpFile) throws FlexPayException {

        BufferedWriter writer = null;

        try {
            FetchRange fetchRange = new FetchRange();
            List<RegistryRecord> records = registryRecordService.listRecordsForExport(registry, fetchRange);

            //noinspection IOResourceOpenedButNotSafelyClosed
            writer = new BufferedWriter(new OutputStreamWriter(fpFile.getOutputStream(), RegistryUtil.EXPORT_FILE_ENCODING));

            writer.write(buildHeader(registryService.readWithContainers(Stub.stub(registry))));
            writer.newLine();

            do {
                for (RegistryRecord record : records) {
                    writer.write(buildRecord(registry, record));
                    writer.newLine();
                }
                fetchRange.nextPage();
            } while ((records = registryRecordService.listRecordsForExport(registry, fetchRange)).size() > 0);

            writer.write(buildFooter(registry));
            writer.newLine();

        } catch (IOException e) {
            log.error("Error with writing export-file for registry", e);
            return null;
        } finally {
            IOUtils.closeQuietly(writer);
        }

        fpFile.updateSize();
        fpFile = fpFileService.update(fpFile);

        registry.setSpFile(fpFile);

        registry = registryService.update(registry);

        return registry;

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
        if (containers.size() ==  0) {
            header.append(RegistryUtil.FIELD_SEPARATOR);
        } else {
            for (RegistryContainer container : containers) {
                header.append(RegistryUtil.FIELD_SEPARATOR).append(container.getData());
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

    protected String buildFooter(Registry registry) {

        StringBuilder footer = new StringBuilder();

        log.debug("Building footer for registry = {}", registry);

        footer.append(RegistryUtil.REGISTRY_FOOTER_MESSAGE_TYPE_CHAR).
                append(RegistryUtil.FIELD_SEPARATOR).
                append(StringUtil.getString(registry.getId()));

        log.debug("File footer = {}", footer.toString());

        return footer.toString();
    }
}
