package org.flexpay.sz.dbf;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class DBFInfo<E> {

	private File originalFile;
	private String dbfFileEncoding = ApplicationConfig.getInstance()
			.getSzDefaultDbfFileEncoding();
	private DBFField[] dbfFields;
	private Map<String, Integer> indMap;

	public DBFInfo(File originalFile) {
		this.originalFile = originalFile;
	}

	abstract E create(Object[] record) throws DBFException, FileNotFoundException;

	abstract Object[] getRowData(E element) throws DBFException, FileNotFoundException;

	protected int getInd(String name) throws DBFException, FileNotFoundException {
		if (indMap == null) {
			init();
		}

		return indMap.get(name.toLowerCase());
	}

	DBFField[] getDBFFields() throws DBFException, FileNotFoundException {
		if (dbfFields == null) {
			init();
		}

		return dbfFields;
	}

	private void init() throws DBFException, FileNotFoundException {
		InputStream fis = new FileInputStream(originalFile);
		try {
			DBFReader reader = new DBFReader(fis);
			reader.setCharactersetName(dbfFileEncoding);
			int numberOfFields = reader.getFieldCount();
			dbfFields = new DBFField[numberOfFields];
			indMap = new HashMap<String, Integer>(numberOfFields);
			for (int i = 0; i < numberOfFields; i++) {
				DBFField dbfField = reader.getField(i);
				indMap.put(dbfField.getName().toLowerCase().trim(), i);
				dbfFields[i] = dbfField;
			}
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}

	public void setDbfFileEncoding(String dbfFileEncoding) {
		this.dbfFileEncoding = dbfFileEncoding;
	}

	public String getDbfFileEncoding() {
		return dbfFileEncoding;
	}
}
