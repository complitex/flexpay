package org.flexpay.sz.dbf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;

public class SzDbfReader<E, I extends DBFInfo<E>> {
	private String encoding;
	private InputStream is;
	private DBFReader reader;
	private I dbfInfo;

	public SzDbfReader(I dbfInfo, InputStream is, String encoding) {
		this.dbfInfo = dbfInfo;
		this.is = is;
		this.encoding = encoding;
	}

	private void init() throws DBFException {
		reader = new DBFReader(is);
		reader.setCharactersetName(encoding);
	}

	public E read() throws DBFException {
		if (reader == null) {
			init();
		}

		Object[] record = null;
		record = reader.nextRecord();

		return record == null ? null : dbfInfo.create(record);
	}

	public Collection<E> readAll() throws DBFException {
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
}
