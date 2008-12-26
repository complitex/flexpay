package org.flexpay.common.util;

import org.apache.commons.io.FileUtils;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class FPFileUtil {

	public static String getLocalDirPath(String moduleName, Date creationDate) {
		File root = ApplicationConfig.getDataRoot();
        Calendar c = Calendar.getInstance();
        c.setTime(creationDate);
        return root.getPath() + File.separator
                + moduleName + File.separator
                + c.get(Calendar.YEAR) + File.separator
                + (1 + c.get(Calendar.MONTH)) + File.separator
                + c.get(Calendar.DATE) + File.separator;
	}

    public static String getLocalDirPath(FPFile file) {
        return 	getLocalDirPath(file.getModule().getName(), file.getCreationDate());
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
        File fileOnServer = File.createTempFile(getFileNameWithoutExtension(name) + "_", StringUtil.getFileExtension(name), localDir);

		if (file != null && file.length() > 0) {
        	FileUtils.copyFile(file, fileOnServer);
		}

        return fileOnServer;
	}

}
