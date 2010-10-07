package org.flexpay.eirc.sp.impl.validation;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.eirc.sp.impl.MessageLevel;
import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messenger;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.jetbrains.annotations.NotNull;

public class CorrectionsRecordValidator extends MessageValidatorWithContext<String> {

    public static final long FIELDS_LENGTH = 28;
	public static final long FIELDS_LENGTH_SKIP_RECORD = 20;
	public static final long FIELDS_LENGTH_EMPTY_FOOTER = 21;

    private FieldsValidator fieldsValidator;
    private BuildingAddressValidator buildingAddressValidator;
    private LongValidator longValidator;
    private DoubleValidator doubleValidator;
    private ModificationsBeginDateValidator modificationsBeginDateValidator;
    private ServiceCodeValidator serviceCodeValidator;

    public CorrectionsRecordValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);

        fieldsValidator = new FieldsValidator(mess);
        buildingAddressValidator = new BuildingAddressValidator(mess);
        longValidator = new LongValidator(mess, context);
        doubleValidator = new DoubleValidator(mess);
        modificationsBeginDateValidator = new ModificationsBeginDateValidator(mess, context);
        serviceCodeValidator = new ServiceCodeValidator(mess, context);
    }

    @Override
    public boolean validate(@NotNull String line) {
        String[] fields = context.getLineParser().parse(line, messenger);
		if (fields.length == FIELDS_LENGTH_SKIP_RECORD) {
			addErrorMessage("Skip record. Found {} fields. It is closed account", fields.length, MessageLevel.WARN);
			return true;
		}
		if (fields.length > FIELDS_LENGTH_SKIP_RECORD && StringUtils.isEmpty(fields[9]) && StringUtils.isEmpty(fields[10])
				&& !modificationsBeginDateValidator.validate(fields[19])) {
			fields = (String[])ArrayUtils.remove(fields, 9);
			fields[9] = "-";
		}
		if (fields.length < FIELDS_LENGTH && fields.length != FIELDS_LENGTH_EMPTY_FOOTER) {
			addErrorMessage("Found {} fields. expected {}, {} or {}",
					new Object[]{fields.length, FIELDS_LENGTH, FIELDS_LENGTH_EMPTY_FOOTER, FIELDS_LENGTH_SKIP_RECORD});
            return false;
		}
		//if (fields.length > 28) {
		//	log.warn("Found {} fields. expected 28", fields.length);
		//}
		if (!fieldsValidator.validate(fields)) {
            return false;
        }
        if (!longValidator.validate(fields[3])) {
            addErrorMessage("Can't parse town id {} ", fields[3]);
            return false;
		}
		if (!longValidator.validate(fields[4])) {
		    addErrorMessage("Can't parse ERC street id {}", fields[4]);
            return false;
		}
		if (!longValidator.validate(fields[5])) {
			addErrorMessage("Can't parse organization street id {}", fields[5]);
            return false;
		}
        if (!buildingAddressValidator.validate(fields[8])) {
            return false;
        }
		if (StringUtils.isEmpty(fields[10])) {
			addErrorMessage("Full square is empty. Set '0'", MessageLevel.WARN);
		} else if (!doubleValidator.validate(fields[10])) {
			addErrorMessage("Can't parse full square {}", fields[10]);
            return false;
		}
		if (StringUtils.isEmpty(fields[11])) {
			addErrorMessage("Living space is empty. Set '0'", MessageLevel.WARN);
		} else if (!doubleValidator.validate(fields[11])) {
			addErrorMessage("Can't parse living space {}", fields[11]);
            return false;
		}
		if (StringUtils.isEmpty(fields[12])) {
			addErrorMessage("Balcony square is empty. Set '0'", MessageLevel.WARN);
		} else if (!doubleValidator.validate(fields[12])) {
			addErrorMessage("Can't parse balcony square {}", fields[12]);
            return false;
		}
		if (StringUtils.isEmpty(fields[13])) {
			addErrorMessage("Loggia square is empty. Set '0'", MessageLevel.WARN);
		} else if (!doubleValidator.validate(fields[13])) {
			addErrorMessage("Can't parse loggia square {}", fields[13]);
            return false;
		}
		if (!longValidator.validate(fields[14])) {
			addErrorMessage("Can't parse registered persons number {}", fields[14]);
            return false;
		}
		if (!longValidator.validate(fields[15])) {
			addErrorMessage("Can't parse resident persons number {}", fields[15]);
            return false;
		}
		if (!longValidator.validate(fields[16])) {
			addErrorMessage("Can't parse privileged persons number {}", fields[16]);
            return false;
		}
		if (!longValidator.validate(fields[17])) {
			addErrorMessage("Can't parse privileged persons number {}", fields[17]);
            return false;
		}
		if (!modificationsBeginDateValidator.validate(fields[19])) {
			addErrorMessage("Can't parse modifications begin date {}", fields[19]);
            return false;
		}

		if (!serviceCodeValidator.validate(fields[20])) {
            return false;
        }

		if (fields.length == FIELDS_LENGTH_EMPTY_FOOTER) {
			addErrorMessage("Skip rest data (rooms quantity, etc). Found {} fields.", fields.length, MessageLevel.WARN);
			return true;
		}

		if (!fields[21].equals("0") && !fields[21].equals("1")) {
			addErrorMessage("Invalid sign of lift availability {}", fields[21]);
            return false;
		}
		if (!longValidator.validate(fields[22])) {
			addErrorMessage("Can't parse rooms quantity {}", fields[22]);
            return false;
		}
		if (!fields[23].equals("0") && !fields[23].equals("1")) {
			addErrorMessage("Invalid sign of floor electric furnaces availability {}", fields[23]);
            return false;
		}
		if (!fields[24].equals("0") && !fields[24].equals("1")
			&& !fields[24].equals("2") && !fields[24].equals("3")) {
			addErrorMessage("Invalid view property {}", fields[24]);
            return false;
		}
		if (!fields[25].equals("0") && !fields[25].equals("1")
			&& !fields[25].equals("2") && !fields[25].equals("3")) {
			addErrorMessage("Invalid sign of well {}", fields[25]);
            return false;
		}
        return true;
    }
}
