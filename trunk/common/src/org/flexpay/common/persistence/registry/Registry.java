package org.flexpay.common.persistence.registry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.file.FPFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;

/**
 * Registry header for flexpay exchange file
 */
public class Registry extends DomainObject {

	private Long registryNumber;
	private Long recordsNumber;
	private Date creationDate;
	private Date fromDate;
	private Date tillDate;
	private Long senderCode;
	private Long recipientCode;
	private BigDecimal amount;
    private int errorsNumber = -1;

	private RegistryType registryType;
	private RegistryStatus registryStatus;
	private RegistryArchiveStatus archiveStatus;
	private RegistryProperties properties;

	private FPModule module;
    private ImportError importError;

	private List<RegistryContainer> containers = list();
    private Map<RegistryFPFileType, FPFile> files = map();

	public Registry() {
	}

	public Registry(Long id) {
		super(id);
	}

	public RegistryStatus getRegistryStatus() {
		return registryStatus;
	}

	public void setRegistryStatus(RegistryStatus registryStatus) {
		this.registryStatus = registryStatus;
	}

	public RegistryType getRegistryType() {
		return registryType;
	}

	public void setRegistryType(RegistryType registryType) {
		this.registryType = registryType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getTillDate() {
		return tillDate;
	}

	public void setTillDate(Date tillDate) {
		this.tillDate = tillDate;
	}

	public Long getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(Long senderCode) {
		this.senderCode = senderCode;
	}

	public Long getRecipientCode() {
		return recipientCode;
	}

	public void setRecipientCode(Long recipientCode) {
		this.recipientCode = recipientCode;
	}

	public List<RegistryContainer> getContainers() {
		return containers;
	}

	public void setContainers(List<RegistryContainer> containers) {
		this.containers = containers;
	}

	public Long getRegistryNumber() {
		return registryNumber;
	}

	public void setRegistryNumber(Long registryNumber) {
		this.registryNumber = registryNumber;
	}

	public Long getRecordsNumber() {
		return recordsNumber;
	}

	public void setRecordsNumber(Long recordsNumber) {
		this.recordsNumber = recordsNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public RegistryArchiveStatus getArchiveStatus() {
		return archiveStatus;
	}

	public void setArchiveStatus(RegistryArchiveStatus archiveStatus) {
		this.archiveStatus = archiveStatus;
	}

	public RegistryProperties getProperties() {
		return properties;
	}

	public void setProperties(RegistryProperties properties) {
		properties.setRegistry(this);
		this.properties = properties;
	}

    public Map<RegistryFPFileType, FPFile> getFiles() {
        return files;
    }

    public void setFiles(Map<RegistryFPFileType, FPFile> files) {
        this.files = files;
    }

	public ImportError getImportError() {
		return importError;
	}

	public void setImportError(ImportError importError) {
		this.importError = importError;
	}

	public int getErrorsNumber() {
		return errorsNumber;
	}

	public void setErrorsNumber(int errorsNumber) {
		this.errorsNumber = errorsNumber;
	}

	public boolean errorsNumberNotInit() {
		return errorsNumber <= 0;
	}

	public FPModule getModule() {
		return module;
	}

	public void setModule(FPModule module) {
		this.module = module;
	}

	public void addContainer(RegistryContainer container) {
		container.setRegistry(this);
		container.setOrder(containers.size());
		containers.add(container);
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("registryNumber", registryNumber).
                append("recordsNumber", recordsNumber).
                append("creationDate", creationDate).
                append("fromDate", fromDate).
                append("tillDate", tillDate).
                append("senderCode", senderCode).
                append("recipientCode", recipientCode).
                append("amount", amount).
                append("errorsNumber", errorsNumber).
                toString();
    }
}
