package org.flexpay.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.util.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class FPFileUtil {

	private static final Logger LOG = LoggerFactory.getLogger(FPFileUtil.class);

	public static String getLocalDirPath(String moduleName, Date creationDate) {
		File root = ApplicationConfig.getDataRoot();
		Calendar c = Calendar.getInstance();
		c.setTime(creationDate);
		int date = c.get(Calendar.DATE);
		int month = 1 + c.get(Calendar.MONTH);
		return root.getPath() + File.separator
			   + moduleName + File.separator
			   + c.get(Calendar.YEAR) + File.separator
			   + (month < 10 ? "0" : "") + month + File.separator
			   + (date < 10 ? "0" : "") + date + File.separator;
	}

	public static String getLocalDirPath(FPFile file) {
		return getLocalDirPath(file.getModule().getName(), file.getCreationDate());
	}

	/**
	 * Returns local file path
	 *
	 * @param file flexpay file
	 * @return local file path
	 */
	public static String getFileLocalPath(FPFile file) {
		return getLocalDirPath(file.getModule().getName(), file.getCreationDate()) + file.getNameOnServer();
	}

	/**
	 * Returns file
	 *
	 * @param fpFile fpFile
	 * @return file
	 */
	public static File getFileOnServer(FPFile fpFile) {
		if (fpFile == null || fpFile.getNameOnServer() == null) {
			return null;
		}
		return new File(getFileLocalPath(fpFile));
	}

	private static File createFile(FPFile fpFile) throws IOException {

		String name = fpFile.getOriginalName();
		String localPath = getLocalDirPath(fpFile.getModule().getName(), fpFile.getCreationDate());
		File localDir = new File(localPath);
		if (!localDir.exists() && !localDir.mkdirs()) {
			throw new IOException("Failed creating localDir: " + localDir);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("File: {}, File name: {}, Extension: {}", new Object[]{
					StringUtil.getFileNameWithoutExtension(name),
					StringUtil.getFileName(name),
					StringUtil.getFileExtension(name)
			});
		}

		File tmpFile = File.createTempFile(
				StringUtil.getFileNameWithoutExtension(name) + "_",
				StringUtil.getFileExtension(name), localDir);
		fpFile.setNameOnServer(tmpFile.getName());

		return tmpFile;
	}

	/**
	 * Create empty file on file system
	 *
	 * @param fpFile flexpay file
	 * @throws IOException if an error occurred
	 */
	public static void createEmptyFile(FPFile fpFile) throws IOException {
		createFile(fpFile);
	}

	/**
	 * Saves all data from given file to file system
	 *
	 * @param fpFile flexpay file
	 * @param file   to read from
	 * @return new file
	 * @throws IOException if an error occurred
	 */
	public static File saveToFileSystem(FPFile fpFile, File file) throws IOException {

		File fileOnServer = createFile(fpFile);
		if (file != null && file.length() > 0) {
			FileUtils.copyFile(file, fileOnServer);
		}

		return fileOnServer;
	}

	/**
	 * Saves all data from given input stream to file system
	 *
	 * @param fpFile flexpay file
	 * @param is	 Stream to read from
	 * @return new file
	 * @throws IOException if an error occurred
	 */
	@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
	public static File saveToFileSystem(FPFile fpFile, InputStream is) throws IOException {

		File fileOnServer = createFile(fpFile);

		OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOnServer));
		try {
			IOUtils.copyLarge(is, os);
		} finally {
			IOUtils.closeQuietly(os);
		}

		return fileOnServer;
	}
}
