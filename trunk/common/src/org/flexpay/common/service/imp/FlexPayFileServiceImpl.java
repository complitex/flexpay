package org.flexpay.common.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.FlexPayFileDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.persistence.FlexPayFileType;
import org.flexpay.common.service.FlexPayFileService;
import org.flexpay.common.util.FlexPayFileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class FlexPayFileServiceImpl implements FlexPayFileService {

    private Logger logger = Logger.getLogger(getClass());

    private FlexPayFileDao flexPayFileDao;

    public FlexPayFileType getTypeByFileName(String fileName) {
        for (FlexPayFileType type : FlexPayFileType.values()) {
            if (Pattern.compile(type.getMask()).matcher(fileName).matches()) {
                return type;
            }
        }

        return FlexPayFileType.UNKNOWN;
    }

	private void deleteFileOnException(FlexPayFile file) throws FlexPayException {
		String localPath = FlexPayFileUtil.getFileLocalPath(file);
		File fileToDelete = new File(localPath);
		if (fileToDelete.exists()) {
			if (!fileToDelete.delete()) {
				logger.warn("File " + localPath + " exists, but not deleted");
			}
		} else {
			logger.warn("No such file " + localPath);
		}
        try {
            flexPayFileDao.delete(file);
        } catch (Exception e) {
            logger.error("Can't delete file with id = " + file.getId() + " from DB", e);
            throw new FlexPayException(e);
        }
    }

    public FlexPayFile createFile(InputStream is, FlexPayFile file) throws FlexPayException {

        try {
            file = flexPayFileDao.save(file);
            Long size = FlexPayFileUtil.saveToFile(file, is);
            file.setSize(size);
            file = flexPayFileDao.save(file);
        } catch (Exception e) {
            logger.error("Error creating new file", e);
            deleteFileOnException(file);
            throw new FlexPayException(e);
        }
        return file;
    }

    public FlexPayFile updateFile(InputStream is, Long oldFileId, FlexPayFile newFile) throws FlexPayException {
        try {
            try {
                deleteFile(oldFileId);
            } catch (FlexPayException e) {
                // do nothing
            }
            return createFile(is, newFile);
        } catch (FlexPayException e) {
            logger.error("Error creating new file", e);
            deleteFileOnException(newFile);
            throw new FlexPayException(e);
        }
    }

    public void updateFile(InputStream is, FlexPayFile file) throws FlexPayException {

        try {
            String localPath = FlexPayFileUtil.getFileLocalPath(file);
            File fileToDelete = new File(localPath);
            if (!fileToDelete.delete()) {
                logger.warn("Error deleting file: " + localPath);
                throw new FlexPayException("Error deleting file: " + localPath);
            }

            Long size = FlexPayFileUtil.saveToFile(file, is);
            file.setSize(size);
            flexPayFileDao.save(file);
        } catch (Exception e) {
            logger.error("Error creating new file", e);
            deleteFileOnException(file);
            throw new FlexPayException(e);
        }
    }

    public void deleteFile(Long fileId) throws FlexPayException {
        FlexPayFile file = flexPayFileDao.read(fileId);
        if (null == file) {
            logger.warn("No file with id " + fileId + " in database");
            return;
        }
        String localPath = FlexPayFileUtil.getFileLocalPath(file);
        File fileToDelete = new File(localPath);
        if (!fileToDelete.delete()) {
            logger.error("Error deleting file from server: " + localPath);
        }
        flexPayFileDao.delete(file);
	}

    public FlexPayFile getFile(Long fileId) throws FlexPayException {
        if (fileId == null || fileId <= 0) {
            logger.warn("Parameter fileId must be more 0");
            return null;
        }
        FlexPayFile file = flexPayFileDao.read(fileId);
		if (null == file) {
			logger.warn("No user with file id " + fileId + " in database");
			return null;
		}
		return file;
	}

    public InputStream getContent(FlexPayFile file) throws IOException {
		if (null == file) {
			return null;
		}
		String path = FlexPayFileUtil.getFileLocalPath(file);
		return new FileInputStream(path);
	}

    public void setFlexPayFileDao(FlexPayFileDao flexPayFileDao) {
        this.flexPayFileDao = flexPayFileDao;
    }

}
