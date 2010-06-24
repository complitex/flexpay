package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.eirc.sp.impl.*;
import org.jetbrains.annotations.NotNull;


public class ChargesFooterValidator extends MessageValidatorWithContext<String> {
    private static final long FIELDS_LENGTH = 4;
    private FieldsValidator fieldsValidator;
    private LongValidator longValidator;

    public ChargesFooterValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);
        fieldsValidator = new FieldsValidator(mess);
        longValidator = new LongValidator(mess, context);
    }

    @Override
    public boolean validate(@NotNull String line) {
        String[] fields = context.getLineParser().parse(line, messenger);
		if (fields.length != FIELDS_LENGTH) {
			addErrorMessage("Not {} fields", FIELDS_LENGTH);
		}
		if (!fieldsValidator.validate(fields)) {
            return false;
        }
		if (!fields[0].equals(MbParsingConstants.LAST_FILE_STRING_BEGIN)) {
			addErrorMessage("First field must be equals " + MbParsingConstants.LAST_FILE_STRING_BEGIN);
            return false;
		}

        if (!longValidator.validate(fields[1])) {
            addErrorMessage("Can not parse income sum in footer {}", fields[1]);
            return false;
        }
        Long footerSumIncome = (Long)context.getParam().get(LongValidator.LONG_VALUE);
        Long sumIncome = (Long)context.getParam().get(ValidationConstants.INCOME);
        if (!footerSumIncome.equals(sumIncome)) {
            addErrorMessage("Invalid data in file (total income sum in footer not equals with sum of incomes in all lines - {}, but were {})", new Object[]{footerSumIncome, sumIncome});
            return false;
        }

        if (!longValidator.validate(fields[2])) {
            addErrorMessage("Can not parse total saldo in footer {}", fields[2]);
            return false;
        }
        Long footerSumSaldo = (Long)context.getParam().get(LongValidator.LONG_VALUE);
        Long sumSaldo = (Long)context.getParam().get(ValidationConstants.SALDO);
        if (!footerSumSaldo.equals(sumSaldo)) {
            addErrorMessage("Invalid data in file (total saldo sum in footer not equals with sum of saldos in all lines - {}, but were {})", new Object[]{footerSumSaldo, sumSaldo});
            return false;
        }

        if (!longValidator.validate(fields[3])) {
            addErrorMessage("Can not parse count lines in footer {}", fields[3]);
            return false;
        }
        Long footerCountLine = (Long)context.getParam().get(LongValidator.LONG_VALUE);
        Integer countLine = (Integer)context.getParam().get(ValidationConstants.COUNT_LINE);
        if (footerCountLine.intValue() != countLine) {
            addErrorMessage("Invalid data in file (incorrect records number in file - {}, but were {})", new Object[]{footerCountLine, countLine});
            return false;
        }

		return true;
    }
}
