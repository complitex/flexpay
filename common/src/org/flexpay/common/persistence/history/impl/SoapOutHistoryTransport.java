package org.flexpay.common.persistence.history.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.xmlbeans.XmlDateTime;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.transport.OutTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class SoapOutHistoryTransport implements OutTransport {

	private static final Logger log = LoggerFactory.getLogger(SoapOutHistoryTransport.class);

	private String url;

	private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

	/**
	 * Send file to its consumer
	 *
	 * @param file FPFile to send
	 * @throws Exception if failure occurs
	 */
    @Override
	public void send(FPFile file) throws Exception {

		log.debug("Sending file {} to url {}", new Object[] { file.getName(), url});

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

		StringWriter writer = new StringWriter();
		StreamSource source = new StreamSource(new StringReader(xml.toString()));
		StreamResult result = new StreamResult(writer);
		webServiceTemplate.sendSourceAndReceiveToResult(url, source, result);

		log.debug("Sending to {} contents:\n{}", url, xml);

		String response = writer.toString();
		if (!response.contains("OK!")) {			
			log.warn("Error sending file {} to url {}. Response is {}", new Object[] {file.getName(), url, response} );
			throw new Exception("Failed sending file to " + url + ": " + response);
		}
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
