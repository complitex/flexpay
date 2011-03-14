package org.flexpay.eirc.sp;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.eirc.action.TestSpFileCreateAction;
import org.flexpay.eirc.sp.impl.FileValidationSchema;
import org.flexpay.eirc.sp.impl.LineParser;
import org.flexpay.eirc.sp.impl.ServiceValidationFactory;
import org.flexpay.eirc.sp.impl.validation.FileValidator;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestMbCorrectionsFileValidator extends TestSpFileCreateAction {

	@Autowired
    @Qualifier("mbCorrectionsFileValidationSchema")
	private FileValidationSchema mbCorrectionsFileValidationSchema;
    @Autowired
    private ServiceValidationFactory serviceValidationFactory;
    @Autowired
    @Qualifier("mbLineParser")
    private LineParser mbLineParser;

	@Test
	public void validateFile() throws Throwable {
        FPFile newFile = createSpFile("org/flexpay/eirc/sp/20090605m_10.ls");
        System.out.println(newFile.getNameOnServer());
		try {
            FileValidator validator = serviceValidationFactory.createFileValidator(mbCorrectionsFileValidationSchema, mbLineParser, null);
			assertTrue("Validation failed", validator.validate(newFile));
		} finally {
			deleteFile(newFile);
		}
	}

}
