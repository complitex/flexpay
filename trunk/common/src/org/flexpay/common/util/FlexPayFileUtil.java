package org.flexpay.common.util;

import org.apache.commons.io.FileUtils;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class FlexPayFileUtil {

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

    public static String getLocalDirPath(FlexPayFile file) {
        return 	getLocalDirPath(file.getModule().getName(), file.getCreationDate());
    }

    /**
     * Returns local file path
     *
     * @param file flexpay file
     * @return local file path
     */
    public static String getFileLocalPath(FlexPayFile file) {
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
     * @param flexPayFile flexPayFile
     * @return file
     */
    public static File getFileOnServer(FlexPayFile flexPayFile) {
        if (flexPayFile.getNameOnServer() == null) {
            return null;
        }
        return new File(getFileLocalPath(flexPayFile));
    }

	/**
	 * Saves all data from given input stream to file system
	 *
	 * @param flexPayFile flexpay file
	 * @param file to read from
	 * @return number of written bytes
	 * @throws IOException if an error occurred
	 */
	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static File saveToFileSystem(FlexPayFile flexPayFile, File file) throws IOException {
        String name = flexPayFile.getOriginalName();
        String localPath = getLocalDirPath(flexPayFile.getModule().getName(), flexPayFile.getCreationDate());
        File localDir = new File(localPath);
        localDir.mkdirs();
        File fileOnServer = File.createTempFile(getFileNameWithoutExtension(name), StringUtil.getFileExtension(name), localDir);

        FileUtils.copyFile(file, fileOnServer);

        return fileOnServer;
	}

}
