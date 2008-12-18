package org.flexpay.common.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.FlexPayFileDao;
import org.flexpay.common.dao.FlexPayFileStatusDao;
import org.flexpay.common.dao.FlexPayFileTypeDao;
import org.flexpay.common.dao.FlexPayModuleDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.persistence.FlexPayFileStatus;
import org.flexpay.common.persistence.FlexPayFileType;
import org.flexpay.common.persistence.FlexPayModule;
import org.flexpay.common.service.FlexPayFileService;
import org.flexpay.common.util.FlexPayFileUtil;
import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Transactional(readOnly = true)
public class FlexPayFileServiceImpl implements FlexPayFileService {

    @NonNls
    private Logger logger = Logger.getLogger(getClass());

    private FlexPayFileDao flexPayFileDao;
    private FlexPayFileTypeDao flexPayFileTypeDao;
    private FlexPayFileStatusDao flexPayFileStatusDao;
    private FlexPayModuleDao flexPayModuleDao;

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

    @Transactional (readOnly = false)
    public FlexPayFile create(FlexPayFile file) throws FlexPayException {
        flexPayFileDao.create(file);

        if (logger.isDebugEnabled()) {
            logger.debug("Created new FlexPayFile: " + file);
        }

        return file;
    }

    public FlexPayFile update(FlexPayFile file) throws FlexPayException {
        flexPayFileDao.update(file);
        return file;
    }

    @Transactional (readOnly = false)
    public void delete(FlexPayFile file) {
        String localPath = FlexPayFileUtil.getFileLocalPath(file);
        File fileToDelete = new File(localPath);
        if (!fileToDelete.delete()) {
            logger.error("Error deleting file from server: " + localPath);
        }
        flexPayFileDao.delete(file);
    }

    public void deleteFromFileSystem(FlexPayFile file) {
        String localPath = FlexPayFileUtil.getFileLocalPath(file);
        File fileToDelete = new File(localPath);
        if (!fileToDelete.delete()) {
            logger.error("Error deleting file from server: " + localPath);
        }
    }

    public FlexPayFile read(Long fileId) throws FlexPayException {
        FlexPayFile file = flexPayFileDao.readFull(fileId);
		if (file == null) {
			logger.warn("No user with file id " + fileId + " in database");
		}
		return file;
	}

    public List<FlexPayFile> getFilesByModuleName(String moduleName) {
        return flexPayFileDao.listFilesByModuleName(moduleName);
    }

    public FlexPayFileType getTypeByName(String name) {
        List<FlexPayFileType> l = flexPayFileTypeDao.listTypesByName(name);
        return l.isEmpty() ? null : l.get(0);
    }

    public FlexPayFileStatus getStatusByName(String name) {
        List<FlexPayFileStatus> l = flexPayFileStatusDao.listStatusesByName(name);
        return l.isEmpty() ? null : l.get(0);
    }

    public FlexPayModule getModuleByName(String name) {
        List<FlexPayModule> l = flexPayModuleDao.listModulesByName(name);
        return l.isEmpty() ? null : l.get(0);
    }

    @Required
    public void setFlexPayFileDao(FlexPayFileDao flexPayFileDao) {
        this.flexPayFileDao = flexPayFileDao;
    }

    @Required
    public void setFlexPayFileTypeDao(FlexPayFileTypeDao flexPayFileTypeDao) {
        this.flexPayFileTypeDao = flexPayFileTypeDao;
    }

    @Required
    public void setFlexPayFileStatusDao(FlexPayFileStatusDao flexPayFileStatusDao) {
        this.flexPayFileStatusDao = flexPayFileStatusDao;
    }

    @Required
    public void setFlexPayModuleDao(FlexPayModuleDao flexPayModuleDao) {
        this.flexPayModuleDao = flexPayModuleDao;
    }

}
