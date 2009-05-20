package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class SelectCorrectionTypeAction extends FPActionSupport {

	private RegistryRecordService registryRecordService;
	private ClassToTypeRegistry typeRegistry;

	private RegistryRecord record = new RegistryRecord();

	@NotNull
	public String doExecute() throws Exception {
		if (record.getId() == null) {
			addActionError(getText("error.registry.record.not_specified"));
			return ERROR;
		}

		record = registryRecordService.read(record.getId());
		if (record == null) {
			addActionError(getText("error.registry.record.invalid_specified"));
			return ERROR;
		}

		ImportError importError = record.getImportError();
		if (importError != null) {
			int objectType = importError.getObjectType();
			if (typeRegistry.getType(StreetType.class) == objectType ||
					typeRegistry.getType(Street.class) == objectType) {
				return "street";
			}
			if (typeRegistry.getType(BuildingAddress.class) == objectType) {
				return "building";
			}
			if (typeRegistry.getType(org.flexpay.ab.persistence.Apartment.class) == objectType ||
					typeRegistry.getType(BtiApartment.class) == objectType) {
				return "apartment";
			}
			if (typeRegistry.getType(org.flexpay.ab.persistence.Person.class) == objectType) {
				return "person";
			}

			addActionError(getText("error.registry.record.unsupported_error_type"));
		}

		return INPUT;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return ERROR;
	}

	public RegistryRecord getRecord() {
		return record;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

}
