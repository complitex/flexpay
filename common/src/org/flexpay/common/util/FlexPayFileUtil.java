package org.flexpay.common.util;

import org.apache.log4j.Logger;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.persistence.FlexPayModule;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class FlexPayFileUtil {

    private static Logger logger = Logger.getLogger(FlexPayFileUtil.class);

	public static String getLocalDirPath(FlexPayModule module, Date creationDate) {
		File dir = ApplicationConfig.getDataRoot();
        Calendar c = Calendar.getInstance();
        c.setTime(creationDate);
        return dir.getPath() + File.separator
                + module.getName() + File.separator
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
     * Returns file name
     *
     * @param name full file name
     * @return file name without extension
     */
    public static String getFileNameWithoutExtension(String name) {
        if (null == name) {
            return "";
        }
        String fileNameWithoutExtension = "";
        int pos = name.lastIndexOf('.');
        if (pos > 0) {
            fileNameWithoutExtension = name.substring(0, pos);
        }
        return fileNameWithoutExtension;
    }

	/**
	 * Returns local file path
	 *
	 * @param file flexpay file
	 * @return local file path
	 */
	public static String getFileLocalPath(FlexPayFile file) {
		return getLocalDirPath(file.getModule(), file.getCreationDate()) + file.getNameOnServer();
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
    public static FlexPayFile saveToFile(FlexPayFile flexPayFile, InputStream is) throws IOException {
		Long size = 0L;
		OutputStream out = null;
        File fileOnSystem = null;
		try {
            String name = flexPayFile.getOriginalName();
            String localPath = getLocalDirPath(flexPayFile.getModule(), flexPayFile.getCreationDate());
            File localDir = new File(localPath);
            boolean created = localDir.mkdirs();
            if (!created) {
                throw new IOException("Can't create dirs for localDir = " + localDir);
            }
            fileOnSystem = File.createTempFile(getFileNameWithoutExtension(name), getFileExtension(name), localDir);
			byte buf[] = new byte[1024];
			out = new FileOutputStream(fileOnSystem);
			int len;
			while ((len = is.read(buf)) > 0) {
				out.write(buf, 0, len);
				size += len;
			}
			out.flush();
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
        if (fileOnSystem == null) {
            throw new IOException("Error with creating file on system");
        }

        flexPayFile.setSize(size);
        flexPayFile.setNameOnServer(fileOnSystem.getName());
        return flexPayFile;
	}

}
