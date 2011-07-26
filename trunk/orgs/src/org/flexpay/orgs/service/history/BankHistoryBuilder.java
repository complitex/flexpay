package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.orgs.persistence.Bank;
import org.flexpay.orgs.persistence.BankDescription;
import org.jetbrains.annotations.NotNull;

public class BankHistoryBuilder
		extends OrganizationInstanceHistoryBuilder<BankDescription, Bank> {

	public static final int FIELD_BANK_IDENTIFIER_CODE = 3;
	public static final int FIELD_CORESPONDING_ACCOUNT = 4;


    @Override
	protected Bank newInstance() {
		return new Bank();
	}

    @Override
	protected BankDescription newDescriptionInstance() {
		return new BankDescription();
	}

    @Override
	protected void doInstanceDiff(@NotNull Bank org1, @NotNull Bank org2, @NotNull Diff diff) {

		if (!EqualsHelper.strEquals(org1.getBankIdentifierCode(), org2.getBankIdentifierCode())) {
			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_BANK_IDENTIFIER_CODE);
			rec.setOldStringValue(org1.getBankIdentifierCode());
			rec.setNewStringValue(org2.getBankIdentifierCode());
			diff.addRecord(rec);
			log.debug("Added bank identifier code diff: {}", rec);
		}

		if (!EqualsHelper.strEquals(org1.getCorrespondingAccount(), org2.getCorrespondingAccount())) {
			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_CORESPONDING_ACCOUNT);
			rec.setOldStringValue(org1.getCorrespondingAccount());
			rec.setNewStringValue(org2.getCorrespondingAccount());
			diff.addRecord(rec);
			log.debug("Added bank identifier code diff: {}", rec);
		}
	}

	@Override
	protected boolean doInstancePatch(@NotNull Bank org, @NotNull HistoryRecord record) {

		switch (record.getFieldType()) {
			case FIELD_BANK_IDENTIFIER_CODE:
				log.debug("Patching bank identifier code: {}", record);
				org.setBankIdentifierCode(record.getNewStringValueNotNull());
				record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
				return true;
			case FIELD_CORESPONDING_ACCOUNT:
				log.debug("Patching bank corresponding account: {}", record);
				org.setCorrespondingAccount(record.getNewStringValueNotNull());
				record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
				return true;
			default:
				return false;
		}
	}
}
