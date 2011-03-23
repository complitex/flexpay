package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.history.ObjectsSyncerJob;
import org.flexpay.common.persistence.history.impl.XmlHistoryUnPacker;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestProcessHistoryFile extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private FPFileService fileService;
	@Autowired
	private XmlHistoryUnPacker historyUnPacker;
	@Autowired
	private ObjectsSyncerJob objectsSyncerJob;

	@Test
	public void testProcessHistoryFile() throws Exception {

		FPFile fpFile = new FPFile();
		fpFile.setModule(fileService.getModuleByName("common"));
		fpFile.setCreationDate(DateUtil.now());
		fpFile.setNameOnServer(/*BEGIN*/"history-COMMON_INSTANCE-1.xml_4678471140487167330.gz"/*END*/);
		fpFile.setOriginalName("history-COMMON_INSTANCE-1.xml.gz");

		historyUnPacker.unpackHistory(fpFile);

		objectsSyncerJob.execute();
	}
}
