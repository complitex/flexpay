package org.flexpay.sz.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileDownloadAction {
	
	private InputStream inputStream;
	
	private String saveName = "saveName.dbf";

	public String downloadFile() throws Exception {
		
		File file = new File("c:\\34467793.A02");
		inputStream = new FileInputStream(file);
		
		
		return null;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getSaveName() {
		return saveName;
	}
	
}
