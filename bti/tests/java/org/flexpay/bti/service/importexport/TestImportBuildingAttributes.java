package org.flexpay.bti.service.importexport;

import org.springframework.beans.factory.annotation.Autowired;
import org.flexpay.bti.process.BuildingAttributesImportJob;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.apache.commons.io.IOUtils;

import java.util.Date;
import java.util.Map;
import java.io.InputStream;
import java.io.Serializable;

public class TestImportBuildingAttributes extends SpringBeanAwareTestCase {

	@Autowired
	private BuildingAttributesImportJob job;

	@Autowired
	private FPFileService fileService;

	@Test
	public void testImportCSV() throws Exception {

		FPFile fpFile = new FPFile();
		fpFile.setModule(fileService.getModuleByName("bti"));
		fpFile.setCreationDate(new Date());
		fpFile.setDescription("Building attributes file");
		fpFile.setOriginalName("/org/flexpay/bti/service/importexport/26.12.2008.csv");

		InputStream is = getFileStream("org/flexpay/bti/service/importexport/26.12.2008.csv");

		try {
			fpFile = FPFileUtil.saveToFileSystem(fpFile, is);
		} finally {
			IOUtils.closeQuietly(is);
		}

		Map<Serializable, Serializable> params = CollectionUtils.map();
		params.put(BuildingAttributesImportJob.PARAM_FILE_ID, fpFile.getId());
		params.put(BuildingAttributesImportJob.PARAM_IMPORT_DATE, DateUtil.parseBeginDate("2008/12/26"));

		job.execute(params);
	}
}
