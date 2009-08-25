package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.eirc.actions.TestSpFileCreateAction;
import org.flexpay.eirc.sp.impl.validation.MbChargesFileValidator;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMbChargesFileValidator extends TestSpFileCreateAction {

	@Autowired
	private MbChargesFileValidator validator;

	@Test
	public void validateFile() throws Throwable {
		FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10.nch");
		try {
			assertTrue("Validation failed", validator.validate(newFile));
		} finally {
			deleteFile(newFile);
		}
	}
}
