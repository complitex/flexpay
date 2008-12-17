package org.flexpay.common.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.FlexPayFileDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.service.FlexPayFileService;
import org.flexpay.common.util.FlexPayFileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FlexPayFileServiceImpl implements FlexPayFileService {

    private Logger logger = Logger.getLogger(getClass());

    private FlexPayFileDao flexPayFileDao;

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

    public FlexPayFile createFile(InputStream is, FlexPayFile flexPayFile) throws FlexPayException {

        try {
            flexPayFileDao.create(FlexPayFileUtil.saveToFile(flexPayFile, is));
        } catch (Exception e) {
            logger.error("Error creating new file", e);
            deleteFileOnException(flexPayFile);
            throw new FlexPayException(e);
        }
        return flexPayFile;
    }

    public void updateFile(InputStream is, FlexPayFile file) throws FlexPayException {

        try {
            String localPath = FlexPayFileUtil.getFileLocalPath(file);
            File fileToDelete = new File(localPath);
            if (!fileToDelete.delete()) {
                logger.warn("Error deleting file: " + localPath);
                throw new FlexPayException("Error deleting file: " + localPath);
            }

            flexPayFileDao.update(FlexPayFileUtil.saveToFile(file, is));
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
