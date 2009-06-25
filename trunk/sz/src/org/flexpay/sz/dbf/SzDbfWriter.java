package org.flexpay.sz.dbf;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.util.io.InputStreamCallback;
import org.flexpay.common.util.io.OutputStreamCallback;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

public class SzDbfWriter<E, I extends DBFInfo<E>> {

	private FPFile file;
	private String encoding;
	private boolean isInSyncMode;
	private DBFWriter writer;
	private I dbfInfo;
	private File rawFile;

	public SzDbfWriter(I dbfInfo, FPFile file, String encoding, boolean isInSyncMode) {
		this.dbfInfo = dbfInfo;
		this.file = file;
		this.encoding = encoding;
		this.isInSyncMode = isInSyncMode;
	}

	private void init() throws IOException {
		if (isInSyncMode) {
			rawFile = File.createTempFile(file.getFileName(), file.getExtension());
			@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
			final OutputStream os = new BufferedOutputStream(new FileOutputStream(rawFile));
			try {
				file.withInputStream(new InputStreamCallback() {
					public void read(InputStream is) throws IOException {
						IOUtils.copyLarge(is, os);
					}
				});
			} finally {
				IOUtils.closeQuietly(os);
			}
			writer = new DBFWriter(rawFile);
		} else {
			writer = new DBFWriter();
		}
		writer.setCharactersetName(encoding);
		DBFField[] dbfFieldArray = dbfInfo.getDBFFields();
		writer.setFields(dbfFieldArray);
	}

	public void write(E element) throws IOException {
		if (writer == null) {
			init();
		}
		writer.addRecord(dbfInfo.getRowData(element));
	}

	public void write(Collection<E> elements) throws IOException {
		for (E element : elements) {
			write(element);
		}
	}

	public void write(Iterator<E> iterator) throws IOException {
		E element;
		while ((element = iterator.next()) != null) {
			write(element);
		}
	}

	public void close() throws IOException {
		if (isInSyncMode) {
			writer.write();
			@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
			final InputStream is = new BufferedInputStream(new FileInputStream(rawFile));
			try {
			file.withOutputStream(new OutputStreamCallback() {
				public void write(OutputStream os) throws IOException {
					IOUtils.copyLarge(is, os);
				}
			});
			} finally {
				IOUtils.closeQuietly(is);
			}
		} else {
			OutputStream os = file.getOutputStream();
			try {
				writer.write(os);
			} finally {
				try {
					os.close();
				} catch (IOException ioe) {
					// ignore
				}
			}
		}
	}
}
