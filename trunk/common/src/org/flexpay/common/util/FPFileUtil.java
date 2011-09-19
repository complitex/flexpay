package org.flexpay.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

	private static final Object MKDIR_MONITOR = new Object();

	private static File createFile(@NotNull FPFile fpFile) throws IOException {

		String name = fpFile.getOriginalName();
		String localPath = getLocalDirPath(fpFile.getModule().getName(), fpFile.getCreationDate());
		File localDir = new File(localPath);
		synchronized (MKDIR_MONITOR) {
			if (!localDir.exists() && !localDir.mkdirs()) {
				throw new IOException("Failed creating localDir: " + localDir);
			}
		}

		String nameWithoutExt = StringUtil.getFileNameWithoutExtension(name);
		String ext = StringUtil.getFileExtension(name);

		if (LOG.isDebugEnabled()) {
			LOG.debug("File: {}, File name: {}, Extension: {}", new Object[] {nameWithoutExt, StringUtil.getFileName(name), ext});
		}

		String fileName = nameWithoutExt + "_";
		// see File.createTempFile
		if (fileName.length() < 3) {
			fileName += "__";
		}
		File tmpFile = File.createTempFile(fileName, ext, localDir);
		fpFile.setNameOnServer(tmpFile.getName());

		return tmpFile;
	}

	/**
	 * Create empty file on file system
	 *
	 * @param fpFile flexpay file
	 * @throws IOException if an error occurred
	 */
	public static void createEmptyFile(@NotNull FPFile fpFile) throws IOException {
		createFile(fpFile);
	}

	/**
	 * Saves all data from given file to file system
	 *
	 * @param file   to read from
	 * @param fpFile flexpay file
	 * @throws IOException if an error occurred
	 */
    public static void copy(@Nullable File file, @NotNull FPFile fpFile) throws IOException {

        LOG.debug("Trying create file on server");
        File fileOnServer = createFile(fpFile);
        LOG.debug("File on server created: {}", fileOnServer);
        if (file != null && file.length() > 0) {
            LOG.debug("Copy file to file on server...");
            FileUtils.copyFile(file, fileOnServer);
            LOG.debug("File copy complete");
        }
    }

	/**
	 * Saves all data from given input stream to file system
	 *
	 * @param is	 Stream to read from
	 * @param fpFile flexpay file
	 * @throws IOException if an error occurred
	 */
	@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
	public static void copy(@NotNull InputStream is, @NotNull FPFile fpFile) throws IOException {

		File fileOnServer = createFile(fpFile);

		OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOnServer));
		try {
			IOUtils.copyLarge(is, os);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
}
