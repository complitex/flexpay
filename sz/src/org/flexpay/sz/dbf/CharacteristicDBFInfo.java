package org.flexpay.sz.dbf;

import com.linuxense.javadbf.DBFException;
import org.flexpay.sz.persistence.CharacteristicRecord;

import java.io.File;
import java.io.FileNotFoundException;

public class CharacteristicDBFInfo extends DBFInfo<CharacteristicRecord> {

	public CharacteristicDBFInfo(File originalFile) {
		super(originalFile);
	}

	CharacteristicRecord create(Object[] rowData) throws DBFException,
			FileNotFoundException {
		CharacteristicRecord characteristic = new CharacteristicRecord();
		characteristic.setCod((Double) rowData[getInd("cod")]);
		characteristic.setCdpr((Double) rowData[getInd("cdpr")]);
		characteristic.setNcard((Double) rowData[getInd("ncard")]);
		characteristic.setIdcode(((String) rowData[getInd("idcode")]).trim());
		characteristic.setPasp(((String) rowData[getInd("pasp")]).trim());
		characteristic.setFio(((String) rowData[getInd("fio")]).trim());
		characteristic.setIdpil(((String) rowData[getInd("idpil")]).trim());
		characteristic.setPasppil(((String) rowData[getInd("pasppil")]).trim());
		characteristic.setFiopil(((String) rowData[getInd("fiopil")]).trim());
		characteristic.setIdx((Double) rowData[getInd("index")]);
		characteristic.setCdul((Double) rowData[getInd("cdul")]);
		characteristic.setHouse(((String) rowData[getInd("house")]).trim());
		characteristic.setBuild(((String) rowData[getInd("build")]).trim());
		characteristic.setApt(((String) rowData[getInd("apt")]).trim());
		characteristic.setVl((Double) rowData[getInd("vl")]);
		characteristic.setPlzag((Double) rowData[getInd("plzag")]);
		characteristic.setPlopal((Double) rowData[getInd("plopal")]);

		return characteristic;
	}

	Object[] getRowData(CharacteristicRecord characteristic) throws DBFException,
			FileNotFoundException {
		Object[] rowData = new Object[getDBFFields().length];
		rowData[getInd("cod")] = characteristic.getCod();
		rowData[getInd("cdpr")] = characteristic.getCdpr();
		rowData[getInd("ncard")] = characteristic.getNcard();
		rowData[getInd("idcode")] = characteristic.getIdcode();
		rowData[getInd("pasp")] = characteristic.getPasp();
		rowData[getInd("fio")] = characteristic.getFio();
		rowData[getInd("idpil")] = characteristic.getIdpil();
		rowData[getInd("pasppil")] = characteristic.getPasppil();
		rowData[getInd("fiopil")] = characteristic.getFiopil();
		rowData[getInd("index")] = characteristic.getIdx();
		rowData[getInd("cdul")] = characteristic.getCdul();
		rowData[getInd("house")] = characteristic.getHouse();
		rowData[getInd("build")] = characteristic.getBuild();
		rowData[getInd("apt")] = characteristic.getApt();
		rowData[getInd("vl")] = characteristic.getVl();
		rowData[getInd("plzag")] = characteristic.getPlzag();
		rowData[getInd("plopal")] = characteristic.getPlopal();

		return rowData;
	}
}
