package org.flexpay.common.process.handler2;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.service.FPFileService;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;

public class FTPUploadWorkItemHandler extends TaskHandler {

    public final static String FILE = "file";
	public final static String FILE_ID = "fileId";
    public final static String REMOTE_FILE_PATH = "remoteFilePath";
    public final static String USER = "user";
    public final static String PASSWORD = "password";
    public final static String HOST = "host";
    public final static String PORT = "port";

    // required services
	private FPFileService fpFileService;

    @Override
    public String execute(Map<String, Object> parameters) throws FlexPayException {
        String remoteFilePath = (String) parameters.get(REMOTE_FILE_PATH);
        String user = (String) parameters.get(USER);
        String password = (String) parameters.get(PASSWORD);
        String host = (String) parameters.get(HOST);
        String port = (String) parameters.get(PORT);

        FPFile file = getFile(parameters);
		if (file == null) {
			log.error("File was not found as a job parameter");
			return RESULT_ERROR;
		}

        FTPClient client = new FTPClient();
        try {
            if (port != null) {
                client.connect(host, Integer.parseInt(port));
            } else {
                client.connect(host);
            }
            int reply = client.getReplyCode();

            log.debug("Connected code: {}", reply);

            if (FTPReply.isPositiveCompletion(reply)) {

                if (client.login(user, password)) {

                    log.debug("Login code: {}", client.getReplyCode());

                    client.setFileType(FTP.BINARY_FILE_TYPE);
                    boolean sent = client.storeFile(remoteFilePath, file.getInputStream());
                    log.debug("Sent code: {}", client.getReplyCode());
                    client.logout();
                    return sent? RESULT_NEXT: RESULT_ERROR;
                }
            }

        } catch (Exception ex) {
            log.error("Failed store file '{}' to host '{}' port '{}' remote filepath {} by user '{}': {}",
                    new Object[]{file, host, port, remoteFilePath, user, ex});
        } finally {
            try {
                if (client.isConnected()) {
                    client.disconnect();
                }
            } catch (IOException e) {

            }
        }

        return RESULT_ERROR;
    }

    protected FPFile getFile(Map<String, Object> parameters) {

		FPFile spFile = null;

		if (parameters.containsKey(FILE)) {
			Object o = parameters.get(FILE);
			if (o instanceof FPFile && ((FPFile) o).getId() != null) {
				spFile = (FPFile) o;
				spFile = fpFileService.read(stub(spFile));
			} else {
				log.error("Invalid file`s instance class");
			}
		} else if (parameters.containsKey(FILE_ID)) {
			Long fileId = (Long) parameters.get(FILE_ID);
			spFile = fpFileService.read(new Stub<FPFile>(fileId));
		}

		return spFile;
	}

    @Required
    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }
}
