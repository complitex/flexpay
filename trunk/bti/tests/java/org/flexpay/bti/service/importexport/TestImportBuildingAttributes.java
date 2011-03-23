package org.flexpay.bti.service.importexport;

import org.apache.commons.io.IOUtils;
import org.flexpay.bti.process.BuildingAttributesImportJob;
import org.flexpay.bti.test.BtiSpringBeanAwareTestCase;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.FPFileUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestImportBuildingAttributes extends BtiSpringBeanAwareTestCase {

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
		fpFile.setUserName("--TEST USER--");

		InputStream is = getFileStream("org/flexpay/bti/service/importexport/26.12.2008.csv");

		try {
			FPFileUtil.copy(is, fpFile);
			fileService.create(fpFile);
			log.info("File uploaded {}", fpFile);

			assertFalse("saveToFileSystem failed, file is still new", fpFile.isNew());
		} finally {
			IOUtils.closeQuietly(is);
		}

		Map<Serializable, Serializable> params = CollectionUtils.map();
		params.put(BuildingAttributesImportJob.PARAM_FILE_ID, fpFile.getId());
		params.put(BuildingAttributesImportJob.PARAM_IMPORT_DATE, DateUtil.parseBeginDate("2008/12/26"));

		assertEquals("Import failed", Job.RESULT_NEXT, job.execute(params));
	}
}
