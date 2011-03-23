package org.flexpay.common.service.transport.impl;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.persistence.file.FPFile;
import org.springframework.beans.factory.annotation.Required;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * OutTransport implementation that simply copies source file to configured directory
 */
public class FileOutTransport extends OutTransportBase {

	private String outputDir;

	/**
	 * Send file to its consumer
	 *
	 * @param file FPFile to send
	 */
	public void doSend(FPFile file) throws Exception {

		InputStream is = file.getInputStream();
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			OutputStream os = new BufferedOutputStream(new FileOutputStream(outputDir + file.getOriginalName()));
			try {
				IOUtils.copyLarge(is, os);
			} finally {
				IOUtils.closeQuietly(os);
			}
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	@Required
	public void setOutputDir(String outputDir) {

		if (!outputDir.endsWith("/")) {
			outputDir += "/";
		}
		this.outputDir = outputDir;
	}
}
