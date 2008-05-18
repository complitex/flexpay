package org.flexpay.eirc.sp;

import java.io.IOException;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.*;

public class SpFileUtil {
	private static SpRegistryService spRegistryService;
	private static SpFileParser spFileParser;

	public static void loadToDb(SpFile spFile) throws Exception {

		spFileParser.parse(spFile);
	}

	public static int getNumberOfRecord(Long spFileId) {
		Page<SpRegistry> pager = new Page<SpRegistry>();
		pager.setPageSize(1);
		spRegistryService.findObjects(pager, spFileId);
		return pager.getTotalNumberOfElements();
	}

	public static boolean isLoadedToDb(Long spFileId) {
		return 0 < getNumberOfRecord(spFileId);
	}

	/**
	 * @param spRegistryService
	 *            the spRegistryService to set
	 */
	public void setSpRegistryService(SpRegistryService spRegistryService) {
		SpFileUtil.spRegistryService = spRegistryService;
	}

	public void setSpFileParser(SpFileParser spFileParser) {
		SpFileUtil.spFileParser = spFileParser;
	}
}
