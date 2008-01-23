package org.flexpay.sz.dbf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;

public abstract class DBFInfo<E> {

	private File originalFile;
	private String originalFileEncoding;
	protected DBFField[] dbfFields;

	public DBFInfo(File originalFile, String originalFileEncoding) {
		this.originalFile = originalFile;
		this.originalFileEncoding = originalFileEncoding;
	}

	private void initDBFFields() throws DBFException, FileNotFoundException {
		DBFReader reader = new DBFReader(new FileInputStream(originalFile));
		reader.setCharactersetName(originalFileEncoding);
		int numberOfFields = reader.getFieldCount();
		dbfFields = new DBFField[numberOfFields];
		for (int i = 0; i < numberOfFields; i++) {
			dbfFields[i] = reader.getField(i);
		}
	}

	DBFField[] getDBFFields() throws DBFException, FileNotFoundException {
		if (dbfFields == null) {
			initDBFFields();
		}

		return dbfFields;
	}

	abstract E create(Object[] record);

	abstract Object[] getRowData(E element);

}
