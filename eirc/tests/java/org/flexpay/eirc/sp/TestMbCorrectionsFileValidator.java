package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.eirc.actions.TestSpFileCreateAction;
import org.flexpay.eirc.sp.impl.validation.MbCorrectionsFileValidator;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMbCorrectionsFileValidator extends TestSpFileCreateAction {

	@Autowired
	private MbCorrectionsFileValidator validator;

//	private boolean ignoreInvalidLinesNumber;

	@Test
	public void validateFile() throws Throwable {
		FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10k.ls");
//		FPFile newFile = createSpFile("org/flexpay/eirc/sp/01033_122008.kor");
		try {
			assertTrue("Validation failed", validator.validate(newFile));
		} finally {
			deleteFile(newFile);
		}
	}

//	@Before
//	public void setupIgnoreInvalidLinesNumber() {
//		ignoreInvalidLinesNumber = validator.isIgnoreInvalidLinesNumber();
//		validator.setIgnoreInvalidLinesNumber(true);
//	}
//
//	@After
//	public void restoreIgnoreInvalidLinesNumber() {
//		validator.setIgnoreInvalidLinesNumber(ignoreInvalidLinesNumber);
//	}
}
