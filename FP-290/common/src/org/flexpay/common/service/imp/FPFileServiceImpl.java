package org.flexpay.common.service.imp;

import org.flexpay.common.dao.FPFileDao;
import org.flexpay.common.dao.FPFileStatusDao;
import org.flexpay.common.dao.FPFileTypeDao;
import org.flexpay.common.dao.FPModuleDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.common.persistence.FPFileType;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional(readOnly = true)
public class FPFileServiceImpl implements FPFileService {

    @NonNls
    private Logger log = LoggerFactory.getLogger(getClass());

    private FPFileDao fpFileDao;
    private FPFileTypeDao fpFileTypeDao;
    private FPFileStatusDao fpFileStatusDao;
    private FPModuleDao fpModuleDao;

	private void deleteFileOnException(FPFile file) throws FlexPayException {
		String localPath = FPFileUtil.getFileLocalPath(file);
		File fileToDelete = new File(localPath);
		if (fileToDelete.exists()) {
			if (!fileToDelete.delete()) {
				log.warn("File {} exists, but not deleted", localPath);
			}
		} else {
			log.warn("No such file {}", localPath);
		}
        try {
            fpFileDao.delete(file);
        } catch (Exception e) {
            log.error("Can't delete file with id = " + file.getId() + " from DB", e);
            throw new FlexPayException(e);
        }
    }

    @Transactional (readOnly = false)
    public FPFile create(FPFile file) throws FlexPayException {
        fpFileDao.create(file);

		log.debug("Created new FPFile: {}", file);

        return file;
    }

    public FPFile update(FPFile file) throws FlexPayException {
        fpFileDao.update(file);
        return file;
    }

    @Transactional (readOnly = false)
    public void delete(FPFile file) {
        String localPath = FPFileUtil.getFileLocalPath(file);
        File fileToDelete = new File(localPath);
        if (!fileToDelete.delete()) {
            log.error("Error deleting file from server: {}", localPath);
        }
        fpFileDao.delete(file);
    }

    public void deleteFromFileSystem(FPFile file) {
        String localPath = FPFileUtil.getFileLocalPath(file);
        File fileToDelete = new File(localPath);
        if (!fileToDelete.delete()) {
            log.error("Error deleting file from server: {}", localPath);
        }
    }

    public FPFile read(Long fileId) throws FlexPayException {
        FPFile file = fpFileDao.readFull(fileId);
		if (file == null) {
			log.warn("No user with file id {} in database", fileId);
		}
		return file;
	}

    public File getFileFromFileSystem(Long fileId) throws FlexPayException {
        return FPFileUtil.getFileOnServer(read(fileId));
    }

    public List<FPFile> getFilesByModuleName(String moduleName) {
        return fpFileDao.listFilesByModuleName(moduleName);
    }

    public FPModule getModuleByName(String name) {
        List<FPModule> l = fpModuleDao.listModulesByName(name);
        return l.isEmpty() ? null : l.get(0);
    }

    public FPFileType getTypeByFileName(String fileName, String moduleName) {
        List<FPFileType> types = fpFileTypeDao.listFileTypesByModuleName(moduleName);
        for (FPFileType type : types) {
            Matcher m = Pattern.compile(type.getFileMask()).matcher(fileName);
            if (m.matches()) {
                return type;
            }
        }

        return null;
    }

    public FPFileType getTypeByCode(Long code) {
        List<FPFileType> l = fpFileTypeDao.listTypesByCode(code);
        return l.isEmpty() ? null : l.get(0);
    }

    public FPFileStatus getStatusByCode(Long code) {
        List<FPFileStatus> l = fpFileStatusDao.listStatusesByCode(code);
        return l.isEmpty() ? null : l.get(0);
    }

	@Required
	public void setFpFileDao(FPFileDao fpFileDao) {
		this.fpFileDao = fpFileDao;
	}

	@Required
	public void setFpFileTypeDao(FPFileTypeDao fpFileTypeDao) {
		this.fpFileTypeDao = fpFileTypeDao;
	}

	@Required
	public void setFpFileStatusDao(FPFileStatusDao fpFileStatusDao) {
		this.fpFileStatusDao = fpFileStatusDao;
	}

	@Required
	public void setFpModuleDao(FPModuleDao fpModuleDao) {
		this.fpModuleDao = fpModuleDao;
	}

}
