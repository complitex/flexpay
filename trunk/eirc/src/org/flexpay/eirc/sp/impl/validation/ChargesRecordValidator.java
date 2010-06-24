package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messenger;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.flexpay.eirc.sp.impl.ValidationConstants;
import org.jetbrains.annotations.NotNull;

public class ChargesRecordValidator extends MessageValidatorWithContext<String> {

    private static final long FIELDS_LENGTH = 5;

    private FieldsValidator fieldsValidator;
    private LongValidator longValidator;
    private OperationDateValidator operationDateValidator;
    private ServiceCodeValidator serviceCodeValidator;

    public ChargesRecordValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);

        fieldsValidator = new FieldsValidator(mess);
        longValidator = new LongValidator(mess, context);
        operationDateValidator = new OperationDateValidator(mess, context);
        serviceCodeValidator = new ServiceCodeValidator(mess, context);
    }

    @Override
    public boolean validate(@NotNull String line) {
        String[] fields = context.getLineParser().parse(line, messenger);
		if (fields.length < FIELDS_LENGTH) {
			addErrorMessage("Expected {} fields", FIELDS_LENGTH);
            return false;
		}

        if (!fieldsValidator.validate(fields)) {
            return false;
        }

        if (!longValidator.validate(fields[1])) {
            addErrorMessage("Can't parse charges sum {}", fields[1]);
            return false;
        }
        Long income = (Long)context.getParam().get(LongValidator.LONG_VALUE);
        Long sumIncome = (Long)context.getParam().get(ValidationConstants.INCOME);
		if (sumIncome == null) {
            sumIncome = 0L;
        }
        sumIncome += income;
        context.getParam().put(ValidationConstants.INCOME, sumIncome);

        if (!longValidator.validate(fields[2])) {
            addErrorMessage("Can't parse balance sum {}", fields[2]);
            return false;
        }
        Long saldo = (Long)context.getParam().get(LongValidator.LONG_VALUE);
        Long sumSaldo = (Long)context.getParam().get(ValidationConstants.SALDO);
		if (sumSaldo == null) {
            sumSaldo = 0L;
        }
        sumSaldo += saldo;
        context.getParam().put(ValidationConstants.SALDO, sumSaldo);

		String operationDate = null;
		if (fields.length >= 6) {
			operationDate = fields[5];
		}
        if (!operationDateValidator.validate(operationDate)) {
            return false;
        }
		if (!serviceCodeValidator.validate(fields[3])) {
            return false;
        }
        return true;
    }
}
