package org.flexpay.common.util;

import org.apache.log4j.Logger;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class FlexPayFileUtil {

    private static Logger logger = Logger.getLogger(FlexPayFileUtil.class);

	public static String getLocalDirPath(String module, Date creationDate) {
		File dir = ApplicationConfig.getDataRoot();
        Calendar c = Calendar.getInstance();
        c.setTime(creationDate);
        return dir.getPath() + File.separator
                + module + File.separator
                + c.get(Calendar.YEAR) + File.separator
                + (1 + c.get(Calendar.MONTH)) + File.separator
                + c.get(Calendar.DATE) + File.separator;
	}

	/**
	 * Returns file extension according to given name
     *
	 * @param name file name
	 * @return file extension according to given name
	 */
	public static String getFileExtension(String name) {
		if (null == name) {
			return "";
		}
		String fileExtension = "";
		int pos = name.lastIndexOf('.');
		if (pos > 0) {
			fileExtension = name.substring(pos + 1, name.length());
		}
		return fileExtension;
	}

	/**
	 * Returns local file path
	 *
	 * @param file flexpay file
	 * @return local file path
	 */
	public static String getFileLocalPath(FlexPayFile file) {
		return getLocalDirPath(file.getModule().toString(), file.getCreationDate()) + file.getId() + "." + getFileExtension(file.getOriginalName());
	}

	/**
	 * Saves all data from given input stream to file system
	 *
	 * @param flexPayFile flexpay file
	 * @param is input stream to read data from
	 * @return number of written bytes
	 * @throws IOException if an error occurred
	 */
	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static Long saveToFile(FlexPayFile flexPayFile, InputStream is) throws IOException {
		Long size = 0L;
		OutputStream out = null;
		try {
			String localPath = getLocalDirPath(flexPayFile.getModule().toString(), flexPayFile.getCreationDate());

			File localDir = new File(localPath);
            boolean created = localDir.mkdirs();
            if (!created) {
                String msg = "Can't create dirs for localDir = " + localDir;
                logger.error(msg);
                throw new IOException(msg);
            }
			localPath += flexPayFile.getId() + "." + getFileExtension(flexPayFile.getOriginalName());
			File fileOnSystem = new File(localPath);
			if (!fileOnSystem.createNewFile()) {
				String msg = "Error creating file: " + localPath;
				logger.warn(msg);
				throw new IOException(msg);
			}
			byte buf[] = new byte[1024];
			out = new FileOutputStream(fileOnSystem);
			int len;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
				size += len;
			}
			out.flush();
			return size;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.warn("Error occurred trying to close output stream: ", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("Error occurred trying to close input stream: ", e);
				}
			}
		}
	}

}
