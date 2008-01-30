package org.flexpay.sz.service.imp;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.flexpay.sz.dao.SzFileTypeDao;
import org.flexpay.sz.persistence.SzFileType;
import org.flexpay.sz.service.SzFileTypeService;

public class SzFileTypeServiceImpl implements SzFileTypeService {
	private static Logger log = Logger.getLogger(SzFileTypeServiceImpl.class);

	List<SzFileType> szFileTypeList;

	private SzFileTypeDao szFileTypeDao;

	/**
	 * Read SzFileType object by its unique id
	 * 
	 * @param id
	 *            SzFileType key
	 * @return SzFileType object, or <code>null</code> if object not found
	 */
	public SzFileType read(Long id) {
		return szFileTypeDao.read(id);
	}

	/**
	 * Get a list of available identity types
	 * 
	 * @return List of IdentityType
	 */
	public List<SzFileType> getEntities() {
		return szFileTypeDao.listSzFileTypes();
	}

	public SzFileType getByFileName(String fileName) {
		if (szFileTypeList == null) {
			szFileTypeList = szFileTypeDao.listSzFileTypes();
		}
		for (SzFileType szFileType : szFileTypeList) {
			Matcher m = Pattern.compile(szFileType.getFileMask()).matcher(
					fileName);
			if (m.matches()) {
				return szFileType;
			}
		}

		return null;
	}

	public void setSzFileTypeDao(SzFileTypeDao szFileTypeDao) {
		this.szFileTypeDao = szFileTypeDao;
	}

}
