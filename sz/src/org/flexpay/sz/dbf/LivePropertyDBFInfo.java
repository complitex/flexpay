package org.flexpay.sz.dbf;

import java.io.File;

import org.flexpay.sz.persistence.Characteristic;

public class LivePropertyDBFInfo extends DBFInfo<Characteristic> {
	public static int COD_IND = 0;
	public static int CDPR_IND = 1;
	public static int NCARD_IND = 2;
	public static int IDCODE_IND = 3;
	public static int PASP_IND = 4;
	public static int FIO_IND = 5;
	public static int IDPIL_IND = 6;
	public static int PASPPIL_IND = 7;
	public static int FIOPIL_IND = 8;
	public static int INDEX_IND = 9;
	public static int CDUL_IND = 10;
	public static int HOUSE_IND = 11;
	public static int BLILD_IND = 12;
	public static int APT_IND = 13;
	public static int VL_IND = 14;
	public static int PLZAG_IND = 15;
	public static int PLOPAL_IND = 16;

	public LivePropertyDBFInfo(File originalFile, String originalFileEncoding) {
		super(originalFile, originalFileEncoding);
	}

	Characteristic create(Object[] record) {
		Characteristic liveProperty = new Characteristic();
		liveProperty.setCod((Double) record[COD_IND]);
		liveProperty.setCdpr((Double) record[CDPR_IND]);
		liveProperty.setNcard((Double) record[NCARD_IND]);
		liveProperty.setIdcode((String) record[IDCODE_IND]);
		liveProperty.setPasp((String) record[PASP_IND]);
		liveProperty.setFio((String) record[FIO_IND]);
		liveProperty.setIdpil((String) record[IDPIL_IND]);
		liveProperty.setPasppil((String) record[PASPPIL_IND]);
		liveProperty.setFiopil((String) record[FIOPIL_IND]);
		liveProperty.setIdx((Double) record[INDEX_IND]);
		liveProperty.setCdul((Double) record[CDUL_IND]);
		liveProperty.setHouse((String) record[HOUSE_IND]);
		liveProperty.setBlild((String) record[BLILD_IND]);
		liveProperty.setApt((String) record[APT_IND]);
		liveProperty.setVl((Double) record[VL_IND]);
		liveProperty.setPlzag((Double) record[PLZAG_IND]);
		liveProperty.setPlopal((Double) record[PLOPAL_IND]);

		return liveProperty;
	}

	Object[] getRowData(Characteristic liveProperty) {
		Object[] rowData = new Object[dbfFields.length];
		rowData[COD_IND] = liveProperty.getCod();
		rowData[CDPR_IND] = liveProperty.getCdpr();
		rowData[NCARD_IND] = liveProperty.getNcard();
		rowData[IDCODE_IND] = liveProperty.getIdcode();
		rowData[PASP_IND] = liveProperty.getPasp();
		rowData[FIO_IND] = liveProperty.getFio();
		rowData[IDPIL_IND] = liveProperty.getIdpil();
		rowData[PASPPIL_IND] = liveProperty.getPasppil();
		rowData[FIOPIL_IND] = liveProperty.getFiopil();
		rowData[INDEX_IND] = liveProperty.getIdx();
		rowData[CDUL_IND] = liveProperty.getCdul();
		rowData[HOUSE_IND] = liveProperty.getHouse();
		rowData[BLILD_IND] = liveProperty.getBlild();
		rowData[APT_IND] = liveProperty.getApt();
		rowData[VL_IND] = liveProperty.getVl();
		rowData[PLZAG_IND] = liveProperty.getPlzag();
		rowData[PLOPAL_IND] = liveProperty.getPlopal();

		return rowData;
	}
}
