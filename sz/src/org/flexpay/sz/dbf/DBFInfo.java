package org.flexpay.sz.dbf;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.sz.util.config.ApplicationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class DBFInfo<E> {

	private FPFile originalFile;
	private String dbfFileEncoding = ApplicationConfig.getSzDefaultDbfFileEncoding();
	private DBFField[] dbfFields;
	private Map<String, Integer> indMap;

	public DBFInfo(FPFile originalFile) {
		this.originalFile = originalFile;
	}

	abstract E create(Object[] record) throws IOException;

	abstract Object[] getRowData(E element) throws IOException;

	protected int getInd(String name) throws IOException {
		if (indMap == null) {
			init();
		}

		return indMap.get(name.toLowerCase());
	}

	DBFField[] getDBFFields() throws IOException {
		if (dbfFields == null) {
			init();
		}

		return dbfFields;
	}

	private void init() throws IOException {
		InputStream fis = originalFile.getInputStream();
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
