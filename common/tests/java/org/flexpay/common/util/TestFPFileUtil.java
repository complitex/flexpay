package org.flexpay.common.util;

import org.flexpay.common.util.config.ApplicationConfig;
import org.junit.Test;

import java.io.File;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class TestFPFileUtil {

	@Test
	public void testGetLocalDirPath() {

		String moduleName = "module";

		Calendar c = Calendar.getInstance();
		c.set(2010, Calendar.JANUARY, 1);
		String path1 = getPath(moduleName, "2010", "01", "01");
		assertEquals("1. Local path has incorrect length!", path1, FPFileUtil.getLocalDirPath(moduleName, c.getTime()));
		c.set(2010, Calendar.NOVEMBER, 2);
		String path2 = getPath(moduleName, "2010", "11", "02");
		assertEquals("2. Local path has incorrect length!", path2, FPFileUtil.getLocalDirPath(moduleName, c.getTime()));
		c.set(2010, Calendar.JANUARY, 23);
		String path3 = getPath(moduleName, "2010", "01", "23");
		assertEquals("3. Local path has incorrect length!", path3, FPFileUtil.getLocalDirPath(moduleName, c.getTime()));
		c.set(2010, Calendar.DECEMBER, 22);
		String path4 = getPath(moduleName, "2010", "12", "22");
		assertEquals("4. Local path has incorrect length!", path4, FPFileUtil.getLocalDirPath(moduleName, c.getTime()));
	}

	private String getPath(String moduleName, String year, String month, String date) {

		File root = ApplicationConfig.getDataRoot();

		return root.getAbsolutePath() + File.separator +
					   moduleName + File.separator +
					   year + File.separator +
					   month + File.separator +
					   date + File.separator;
	}

}
