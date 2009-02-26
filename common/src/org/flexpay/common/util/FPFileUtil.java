package org.flexpay.common.util;

import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.util.config.ApplicationConfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
		int date = 1 + c.get(Calendar.DATE);
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

	/**
	 * Saves all data from given input stream to file system
	 *
	 * @param fpFile flexpay file
	 * @param file to read from
	 * @return number of written bytes
	 * @throws IOException if an error occurred
	 */
	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static File saveToFileSystem(FPFile fpFile, File file) throws IOException {
        String name = fpFile.getOriginalName();
        String localPath = getLocalDirPath(fpFile.getModule().getName(), fpFile.getCreationDate());
        File localDir = new File(localPath);
        localDir.mkdirs();
        File fileOnServer = File.createTempFile(
				StringUtil.getFileNameWithoutExtension(name) + "_",
				StringUtil.getFileExtension(name), localDir);

		if (file != null && file.length() > 0) {
        	FileUtils.copyFile(file, fileOnServer);
		}

        return fileOnServer;
	}

	public static File saveToFileSystem(FPFile fpFile, InputStream is) throws IOException {
		String name = fpFile.getOriginalName();
		String localPath = getLocalDirPath(fpFile.getModule().getName(), fpFile.getCreationDate());
		File localDir = new File(localPath);
		if (!localDir.exists() && !localDir.mkdirs()) {
			throw new IOException("Failed creating localDir: " + localDir);
		}

		LOG.debug("File: {}", StringUtil.getFileNameWithoutExtension(name));
		LOG.debug("File name: {}", StringUtil.getFileName(name));
		LOG.debug("Extension: {}", StringUtil.getFileExtension(name));

		File fileOnServer = File.createTempFile(
				StringUtil.getFileNameWithoutExtension(name) + "_",
				StringUtil.getFileExtension(name), localDir);

		OutputStream os = new BufferedOutputStream(new FileOutputStream(fileOnServer));
		try {
			IOUtils.copyLarge(is, os);
		} finally {
			IOUtils.closeQuietly(os);
		}

		return fileOnServer;
	}
}
