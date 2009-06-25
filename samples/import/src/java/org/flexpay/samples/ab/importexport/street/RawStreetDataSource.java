package org.flexpay.samples.ab.importexport.street;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.service.importexport.RawDataSource;

import java.io.*;
import java.util.List;

/**
 * Sample RawStreedData data source, reads a file of ' Street Name | Type ' formatted
 * lines street descriptions.
 */
public class RawStreetDataSource implements RawDataSource<RawStreetData> {

	private static final String FILE_NAME = System.getProperty("java.io.tempdir") + "/streets.txt";

	private DataInputStream inputStream;
	private LineNumberReader br;

	/**
	 * Check if source is trusted and new objects are allowed to be created from this source
	 *
	 * @return <code>true</code> if the source is trusted, or <code>false</code> otherwise
	 */
	public boolean trusted() {
		return true;
	}

	/**
	 * Find raw data by its id is not supported
	 *
	 * @param objId Raw data id
	 * @return raw data
	 */
	public RawStreetData getById(String objId) {
		throw new RuntimeException("Not implemented");
	}

	/**
	 * Initialize data source
	 */
	public void initialize() {
		try {
			inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(FILE_NAME)));
			//noinspection IOResourceOpenedButNotSafelyClosed
			br = new LineNumberReader(new InputStreamReader(inputStream, "UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException("Failed opening file " + FILE_NAME, e);
		}
	}

	/**
	 * Release all resources taken
	 */
	public void close() {
		// close file stream opened
		IOUtils.closeQuietly(inputStream);
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other words, returns
	 * <tt>true</tt> if <tt>next</tt> would return an element rather than throwing an
	 * exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	public boolean hasNext() {
		try {
			// check if a stream contains any characters
			br.mark(1);
			int i = br.read();
			br.reset();
			return i != -1;
		} catch (IOException e) {
			throw new RuntimeException("Failed checking for next", e);
		}
	}

	/**
	 * Returns the next new imported element in the iteration.
	 *
	 * @param holder Operation type holder
	 * @return the next element in the iteration.
	 * @throws java.util.NoSuchElementException
	 *          iteration has no more elements.
	 */
	public RawStreetData next(ImportOperationTypeHolder holder) {
		try {
			String line = br.readLine();
			String[] parts = StringUtils.split(line, '|');

			RawStreetData data = new RawStreetData();
			// id of the data is simply a line number in a file
			data.setExternalSourceId(String.valueOf(br.getLineNumber()));
			// street name is a part before '|'
			data.addNameValuePair(RawStreetData.FIELD_NAME, parts[0].trim());
			// street type is a part after '|'
			data.addNameValuePair(RawStreetData.FIELD_TYPE, parts[1].trim());

			return data;
		} catch (IOException e) {
			throw new RuntimeException("Cannot read line", e);
		}
	}

	/**
	 * return next batch of data
	 *
	 * @return List of raw data, when the list is empty hasNext() should return <code>false</code>
	 */
	public List<RawStreetData> nextPage() {
		throw new RuntimeException("Not implemented");
	}
}
