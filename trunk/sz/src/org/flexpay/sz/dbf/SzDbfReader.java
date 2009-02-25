package org.flexpay.sz.dbf;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SzDbfReader<E, I extends DBFInfo<E>> {

	private File file;
	private InputStream is;
	private DBFReader reader;
	private I dbfInfo;

	public SzDbfReader(I dbfInfo, File file) {
		this.dbfInfo = dbfInfo;
		this.file = file;
	}

	private void init() throws DBFException, FileNotFoundException {
		is = new FileInputStream(file);
		reader = new DBFReader(is);
		reader.setCharactersetName(dbfInfo.getDbfFileEncoding());
	}

	public E read() throws DBFException, FileNotFoundException {
		if (reader == null) {
			init();
		}

		Object[] record = reader.nextRecord();

		return record == null ? null : dbfInfo.create(record);
	}

	public Collection<E> readAll() throws DBFException, FileNotFoundException {
		if (reader == null) {
			init();
		}

		List<E> elements = new ArrayList<E>(reader.getRecordCount());
		E element = null;
		while ((element = read()) != null) {
			elements.add(element);
		}

		return elements;
	}

	public void close() {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// TODO ignore
			}
		}
	}
}
