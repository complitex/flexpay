package org.flexpay.common.util;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.GZIPInputStream;
import java.util.Enumeration;

/**
 * FileSource is a File wrapper that opens a stream from source file.
 * Wrapper able to open streams from ZIP files
 */
public class FileSource {

	private File file = null;
	private ZipFile zipFile = null;
	private boolean isGzip = false;

	public FileSource(File file, String type) throws Exception {
		if ("zip".equalsIgnoreCase(type)) {
			zipFile = new ZipFile(file);
		} else {
			this.file = file;
			if ("gzip".equalsIgnoreCase(type)) {
				isGzip = true;
			}
		}
	}

	public InputStream openStream() throws Exception {
		if (file != null) {
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			if (isGzip) {
				is = new GZIPInputStream(is);
			}
			return is;
		} else {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			if (!entries.hasMoreElements()) {
				throw new IllegalStateException("No entries in zip file: " + zipFile.getName());
			}

			ZipEntry entry = entries.nextElement();
			if (entry.isDirectory()) {
				throw new IllegalStateException("First entry is a directory in zip file: " + zipFile.getName());
			}

			if (entries.hasMoreElements()) {
				throw new IllegalStateException("Too many entries in zip file: " + zipFile.getName());
			}

			return zipFile.getInputStream(entry);
		}
	}

	public void close() throws Exception {
		closeZipFile();
	}

	private void closeZipFile() throws Exception {
		if (zipFile != null) {
			zipFile.close();
			zipFile = null;
		}
	}
}
