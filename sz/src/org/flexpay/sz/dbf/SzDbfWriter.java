package org.flexpay.sz.dbf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;


import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;

public class SzDbfWriter<E, I extends DBFInfo<E>> {

	private File file;
	private String encoding;
	private boolean isInSyncMode;
	private DBFWriter writer;
	private I dbfInfo;

	public SzDbfWriter(I dbfInfo, File file, String encoding,
			boolean isInSyncMode) {
		this.dbfInfo = dbfInfo;
		this.file = file;
		this.encoding = encoding;
		this.isInSyncMode = isInSyncMode;
	}

	private void init() throws DBFException, FileNotFoundException {
		if (isInSyncMode) {
			writer = new DBFWriter(file);
		} else {
			writer = new DBFWriter();
		}
		writer.setCharactersetName(encoding);
		DBFField[] dbfFieldArray = dbfInfo.getDBFFields();
		writer.setFields(dbfFieldArray);
	}

	public void write(E element) throws DBFException, FileNotFoundException {
		if (writer == null) {
			init();
		}
		writer.addRecord(dbfInfo.getRowData(element));
	}

	public void write(Collection<E> elements) throws DBFException,
			FileNotFoundException {
		for (E element : elements) {
			write(element);
		}
	}

	public void write(Iterator<E> iterator) throws DBFException,
			FileNotFoundException {
		E element = null;
		while ((element = iterator.next()) != null) {
			write(element);
		}
	}

	public void close() throws DBFException, FileNotFoundException {
		if (isInSyncMode) {
			writer.write();
		} else {
			OutputStream os = new FileOutputStream(file);
			try {
				writer.write(os);
			} catch (DBFException e) {
				try {
					os.close();
				} catch (IOException ioe) {
					// ignore
				}
			}
		}
	}
}
