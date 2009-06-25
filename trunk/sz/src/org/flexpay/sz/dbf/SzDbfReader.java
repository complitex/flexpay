package org.flexpay.sz.dbf;

import com.linuxense.javadbf.DBFReader;
import org.flexpay.common.persistence.file.FPFile;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SzDbfReader<E, I extends DBFInfo<E>> {

	private FPFile file;
	private InputStream is;
	private DBFReader reader;
	private I dbfInfo;

	public SzDbfReader(I dbfInfo, FPFile file) {
		this.dbfInfo = dbfInfo;
		this.file = file;
	}

	private void init() throws IOException {
		is = file.getInputStream();
		reader = new DBFReader(is);
		reader.setCharactersetName(dbfInfo.getDbfFileEncoding());
	}

	public E read() throws IOException {
		if (reader == null) {
			init();
		}

		Object[] record = reader.nextRecord();

		return record == null ? null : dbfInfo.create(record);
	}

	public Collection<E> readAll() throws IOException {
		if (reader == null) {
			init();
		}

		List<E> elements = new ArrayList<E>(reader.getRecordCount());
		E element;
		while ((element = read()) != null) {
			elements.add(element);
		}

		return elements;
	}

	public void close() {
		IOUtils.closeQuietly(is);
	}
}
