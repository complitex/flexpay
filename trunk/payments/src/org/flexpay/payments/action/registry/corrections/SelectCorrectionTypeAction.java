package org.flexpay.payments.action.registry.corrections;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RecordErrorsGroup;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.payments.action.registry.data.RecordErrorsGroupView;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.DataCorrection.*;
import static org.flexpay.common.persistence.Stub.stub;

public class SelectCorrectionTypeAction extends FPActionSupport {

    private Registry registry = new Registry();
    private RecordErrorsGroup group;
    private RecordErrorsGroupView groupView;
	private RegistryRecord record = new RegistryRecord();
	private String type;

    protected ClassToTypeRegistry typeRegistry;
	private RegistryRecordService registryRecordService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

        if (group != null) {

            Integer objectType = group.getErrorType();

            groupView = new RecordErrorsGroupView(group, typeRegistry);
            
            if (checkStreetType(objectType)) {
                return "correctStreet";
            } else if (checkStreetTypeType(objectType)) {
                return "correctStreetType";
            } else if (checkBuildingType(objectType)) {
                return "correctBuilding";
            } else if (checkApartmentType(objectType)) {
                return "correctApartment";
            } else if (checkPersonType(objectType)) {
                return "correctPerson";
            }
        }

		if (record == null || record.isNew()) {
			log.warn("Incorrect registry record id");
			addActionError(getText("error.registry.record.not_specified"));
			return INPUT;
		}

		Stub<RegistryRecord> stub = stub(record);
		record = registryRecordService.read(stub);
		if (record == null) {
			log.warn("Can't get registry record with id {} from DB", stub.getId());
			addActionError(getText("error.registry.record.invalid_specified"));
			return INPUT;
		}

		ImportError importError = record.getImportError();
		if (importError != null) {
			int objectType = importError.getObjectType();
			if (checkStreetType(objectType)) {
				return "correctStreet";
            } else if (checkStreetTypeType(objectType)) {
                return "correctStreetType";
			} else if (checkBuildingType(objectType)) {
				return "correctBuilding";
			} else if (checkApartmentType(objectType)) {
				return "correctApartment";
			} else if (checkPersonType(objectType)) {
				return "correctPerson";
			}

			addActionError(getText("error.registry.record.unsupported_error_type"));
		} else if (CORRECT_TYPE_STREET.equals(type)) {
			return "correctStreet";
        } else if (CORRECT_TYPE_STREET_TYPE.equals(type)) {
            return "correctStreetType";
		} else if (CORRECT_TYPE_BUILDING.equals(type)) {
			return "correctBuilding";
		} else if (CORRECT_TYPE_APARTMENT.equals(type)) {
			return "correctApartment";
		} else if (CORRECT_TYPE_PERSON.equals(type)) {
			return "correctPerson";
		} else if (type == null) {
			return INPUT;
		} else {
			log.warn("Incorrect type parameter {}", type);
			addActionError(getText("payments.error.registry.incorrect_correction_type"));
		}

		return INPUT;
	}

    protected boolean checkPersonType(int objectType) {
        return typeRegistry.getType(Person.class) == objectType;
    }

    protected boolean checkApartmentType(int objectType) {
        return typeRegistry.getType(Apartment.class) == objectType;
    }

    protected boolean checkBuildingType(int objectType) {
        return typeRegistry.getType(BuildingAddress.class) == objectType;
    }

    protected boolean checkStreetType(int objectType) {
        return typeRegistry.getType(Street.class) == objectType;
    }

    protected boolean checkStreetTypeType(int objectType) {
        return typeRegistry.getType(StreetType.class) == objectType;
    }

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

    public RecordErrorsGroup getGroup() {
        return group;
    }

    public void setGroup(RecordErrorsGroup group) {
        this.group = group;
    }

    public RecordErrorsGroupView getGroupView() {
        return groupView;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public RegistryRecord getRecord() {
		return record;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
	}

	public void setType(String type) {
		this.type = type;
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
