package org.flexpay.sz.dbf;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.sz.persistence.ServiceTypeRecord;

import java.io.IOException;

public class ServiceTypeRecordDBFInfo extends DBFInfo<ServiceTypeRecord> {

	public ServiceTypeRecordDBFInfo(FPFile originalFile) {
		super(originalFile);
	}

	ServiceTypeRecord create(Object[] rowData) throws IOException {

		ServiceTypeRecord record = new ServiceTypeRecord();
		record.setExtDistrictCode(getInteger(rowData, "cod"));
		record.setExtOrganizationCode(getInteger(rowData, "cdpr"));
		record.setDwellingOwnerId(getInteger(rowData, "ncard"));
		record.setDwellingOwnerTaxNumber(getString(rowData, "idcode"));
		record.setDwellingOwnerPasport(getString(rowData, "pasp"));
		record.setDwellingOwnerName(getString(rowData, "fio"));
		record.setDeadheadTaxNumber(getString(rowData, "idpil"));
		record.setDeadheadPassport(getString(rowData, "pasppil"));
		record.setDeadheadName(getString(rowData, "fiopil"));
		record.setPostalCode(getInteger(rowData, "index"));
		record.setExtStreetCode(getInteger(rowData, "cdul"));
		record.setBuildingNumber(getString(rowData, "house"));
		record.setBulkNumber(getString(rowData, "build"));
		record.setApartmentNumber(getString(rowData, "apt"));
		record.setDeadheadCategory(getInteger(rowData, "kat"));
		record.setPrivilegeCode(getInteger(rowData, "lgcode"));
		record.setPrivilegeStartYear(getInteger(rowData, "yearin"));
		record.setPrivilegeStartMonth(getInteger(rowData, "monthin"));
		record.setPrivilegeEndYear(getInteger(rowData, "yearout"));
		record.setPrivilegeEndMonth(getInteger(rowData, "monthout"));
		record.setPersonalAccountNumber(getString(rowData, "rah"));
		record.setServiceType(getInteger(rowData, "rizn"));
		record.setTariffCode(getInteger(rowData, "tarif"));

		return record;
	}

	protected String getString(Object[] rowData, String fieldName) throws IOException {

		int fieldIndex = getInd(fieldName);
		String value = (String) rowData[fieldIndex];
		return value == null ? null : value.trim();
	}

	protected Integer getInteger(Object[] rowData, String fieldName) throws IOException {
		return getInteger(rowData, fieldName, 0);
	}

	protected Integer getInteger(Object[] rowData, String fieldName, int precision) throws IOException {

		int fieldIndex = getInd(fieldName);
		Double val = (Double) rowData[fieldIndex];
		return getInteger(val, precision);
	}

	// hope nobody needs more that 4 digits after the point
	protected static final double[] FACTORS = {1.0, 10.0, 100.0, 1000.0, 10000.0};

	/**
	 * Convert dbf value to integer, initial dbf values is a (N.precision) number
	 *
	 * @param d		 DBF value
	 * @param precigion necessary precision
	 * @return Integer value
	 */
	protected Integer getInteger(final double d, final int precigion) {

		double d1 = d + (0.5 / FACTORS[precigion]);
		d1 *= FACTORS[precigion];
		return (int) Math.floor(d1);
	}

	Object[] getRowData(ServiceTypeRecord element) throws IOException {

		Object[] row = new Object[getDBFFields().length];
		row[getInd("cod")] = (double) element.getExtDistrictCode().intValue();
		row[getInd("cdpr")] = (double) element.getExtOrganizationCode().intValue();
		row[getInd("ncard")] = (double) element.getDwellingOwnerId().intValue();
		row[getInd("idcode")] = element.getDwellingOwnerTaxNumber();
		row[getInd("pasp")] = element.getDwellingOwnerName();
		row[getInd("fio")] = element.getDwellingOwnerName();
		row[getInd("idpil")] = element.getDeadheadTaxNumber();
		row[getInd("pasppil")] = element.getDeadheadPassport();
		row[getInd("fiopil")] = element.getDeadheadName();
		row[getInd("index")] = (double) element.getPostalCode().intValue();
		row[getInd("cdul")] = (double) element.getExtStreetCode().intValue();
		row[getInd("house")] = element.getBuildingNumber();
		row[getInd("build")] = element.getBulkNumber();
		row[getInd("apt")] = element.getApartmentNumber();
		row[getInd("kat")] = (double) element.getDeadheadCategory().intValue();
		row[getInd("lgcode")] = (double) element.getPrivilegeCode().intValue();
		row[getInd("yearin")] = (double) element.getPrivilegeStartYear().intValue();
		row[getInd("monthin")] = (double) element.getPrivilegeStartMonth().intValue();
		row[getInd("yearout")] = (double) element.getPrivilegeEndYear().intValue();
		row[getInd("monthout")] = (double) element.getPrivilegeEndMonth().intValue();
		row[getInd("rah")] = element.getPersonalAccountNumber();
		row[getInd("rizn")] = (double) element.getServiceType().intValue();
		row[getInd("tarif")] = (double) element.getTariffCode().intValue();

		return row;
	}
}
