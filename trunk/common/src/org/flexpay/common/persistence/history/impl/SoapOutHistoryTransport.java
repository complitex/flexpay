package org.flexpay.common.persistence.history.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.xmlbeans.XmlDateTime;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.service.transport.OutTransport;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class SoapOutHistoryTransport implements OutTransport {

	private String url;

	private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

	/**
	 * Send file to its consumer
	 *
	 * @param file FPFile to send
	 * @throws Exception if failure occurs
	 */
	public void send(FPFile file) throws Exception {

		StringBuilder xml = new StringBuilder("<SaveHistoryRequest xmlns=\"http://flexpay.org/schemas/history\">\n");

		String[] parts = file.getOriginalName().split("-");

		String instanceId = parts[1];
		xml.append("\t<instanceId>").append(instanceId).append("</instanceId>\n");

		int dotPos = parts[2].indexOf('.');
		String groupId = parts[2].substring(0, dotPos);
		xml.append("\t<groupId>").append(groupId).append("</groupId>\n");

		XmlDateTime xmlDate = XmlDateTime.Factory.newInstance();
		xmlDate.setDateValue(file.getCreationDate());
		xml.append("\t<created>").append(xmlDate.getStringValue()).append("</created>\n");

		String base64File = getFile(file);
		xml.append("\t<file>").append(base64File).append("</file>\n");

		xml.append("</SaveHistoryRequest>");

		StreamSource source = new StreamSource(new StringReader(xml.toString()));
		StreamResult result = new StreamResult(System.out);
		webServiceTemplate.sendSourceAndReceiveToResult(url, source, result);
	}

	/**
	 * Get Base64 encoded file contents
	 *
	 * @param file FPFile to encode contents of
	 * @return Base64 code file contents
	 * @throws IOException if failure occurs
	 */
	@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
	private String getFile(FPFile file) throws IOException {
		InputStream is = file.getInputStream();
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream(file.getSize().intValue());
			IOUtils.copy(is, os);

			return new String(Base64.encodeBase64(os.toByteArray()), "UTF-8");
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	@Required
	public void setUrl(String url) {
		this.url = url;
	}
}
