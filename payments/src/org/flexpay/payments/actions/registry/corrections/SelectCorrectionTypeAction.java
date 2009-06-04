package org.flexpay.payments.actions.registry.corrections;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class SelectCorrectionTypeAction extends CashboxCookieActionSupport {

	private RegistryRecordService registryRecordService;
	protected ClassToTypeRegistry typeRegistry;

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
			if (checkStreetType(objectType)) {
				return "street";
			}
			if (checkBuildingType(objectType)) {
				return "building";
			}
			if (checkApartmentType(objectType)) {
				return "apartment";
			}
			if (checkPersonType(objectType)) {
				return "person";
			}

			addActionError(getText("error.registry.record.unsupported_error_type"));
		}

		return INPUT;
	}

    protected boolean checkPersonType(int objectType) {
        return typeRegistry.getType(org.flexpay.ab.persistence.Person.class) == objectType;
    }

    protected boolean checkApartmentType(int objectType) {
        return typeRegistry.getType(org.flexpay.ab.persistence.Apartment.class) == objectType;
    }

    protected boolean checkBuildingType(int objectType) {
        return typeRegistry.getType(BuildingAddress.class) == objectType;
    }

    protected boolean checkStreetType(int objectType) {
        return typeRegistry.getType(StreetType.class) == objectType ||
					typeRegistry.getType(Street.class) == objectType;
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
