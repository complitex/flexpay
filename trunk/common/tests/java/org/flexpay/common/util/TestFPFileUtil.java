package org.flexpay.common.util;

import org.flexpay.common.util.config.ApplicationConfig;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Calendar;
import java.io.File;

public class TestFPFileUtil {

	@Test
	public void testGetLocalDirPath() {

		File root = ApplicationConfig.getDataRoot();
		String moduleName = "module";
		int length = root.getAbsolutePath().length() + moduleName.length() + 13;

		Calendar c = Calendar.getInstance();
		c.set(2010, 1, 2);
		assertEquals("1. Local path has incorrect length!", length, FPFileUtil.getLocalDirPath(moduleName, c.getTime()).length());
		c.set(2010, 10, 2);
		assertEquals("2. Local path has incorrect length!", length, FPFileUtil.getLocalDirPath(moduleName, c.getTime()).length());
		c.set(2010, 1, 23);
		assertEquals("3. Local path has incorrect length!", length, FPFileUtil.getLocalDirPath(moduleName, c.getTime()).length());
		c.set(2010, 12, 22);
		assertEquals("4. Local path has incorrect length!", length, FPFileUtil.getLocalDirPath(moduleName, c.getTime()).length());
	}

}
