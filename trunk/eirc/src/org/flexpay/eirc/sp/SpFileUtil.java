package org.flexpay.eirc.sp;

import java.io.IOException;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.SpRegistryTypeService;

public class SpFileUtil {
	private static SpRegistryService spRegistryService;
	private static SpRegistryRecordService spRegistryRecordService;
	private static SpRegistryTypeService spRegistryTypeService;

	public static void loadToDb(SpFile spFile) throws IOException,
			SpFileFormatException, FlexPayException {
		SpFileParser spFileParser = new SpFileParser(spFile);
		spFileParser.setSpRegistryService(spRegistryService);
		spFileParser.setSpRegistryTypeService(spRegistryTypeService);
		spFileParser.setSpRegistryRecordService(spRegistryRecordService);
		spFileParser.parse();
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

	/**
	 * @param spRegistryRecordService
	 *            the spRegistryRecordService to set
	 */
	public void setSpRegistryRecordService(
			SpRegistryRecordService spRegistryRecordService) {
		SpFileUtil.spRegistryRecordService = spRegistryRecordService;
	}

	/**
	 * @param spRegistryTypeService
	 *            the spRegistryTypeService to set
	 */
	public void setSpRegistryTypeService(
			SpRegistryTypeService spRegistryTypeService) {
		SpFileUtil.spRegistryTypeService = spRegistryTypeService;
	}
}
