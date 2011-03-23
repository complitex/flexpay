package org.flexpay.eirc.process.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.FileSource;
import org.flexpay.eirc.sp.SpFileReader;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;

public class GetRegistryMessageActionHandler extends FlexPayActionHandler {
	public static final String PARAM_FILE_ID = "fileId";
    public static final String PARAM_READER = "reader";
	public static final String PARAM_MESSAGES = "messages";

    private static final String RESULT_END = "end";

    private FPFileService fpFileService;

	private Long minReadChars = 32000L;

	@SuppressWarnings ({"unchecked"})
	@Override
    public String execute2(Map<String, Object> parameters) throws FlexPayException {
		log.debug("start action");
		List<SpFileReader.Message> listMessage = (List<SpFileReader.Message>)parameters.get(PARAM_MESSAGES);
		if (listMessage != null && !listMessage.isEmpty()) {
			return RESULT_NEXT;
		}
		if (listMessage == null) {
			listMessage = list();
		}

        Long spFileId = (Long) parameters.get(PARAM_FILE_ID);
		FPFile spFile = fpFileService.read(new Stub<FPFile>(spFileId));
		if (spFile == null) {
			processLog.error("Can't get spFile from DB (id = " + spFileId + ")");
			return RESULT_ERROR;
		}

        try {
            FileSource fileSource = openRegistryFile(spFile);
            InputStream is = fileSource.openStream();


            SpFileReader reader = (SpFileReader)parameters.get(PARAM_READER);
            if (reader == null) {
                reader = new SpFileReader(is);
            }
			reader.setInputStream(is);

			Long startPoint = reader.getPosition();
			SpFileReader.Message message;

			do {
				message = reader.readMessage();
				listMessage.add(message);
			} while (message != null && (reader.getPosition() - startPoint) < minReadChars);
			log.debug("read {} number record", listMessage.size());
			reader.setInputStream(null);

            parameters.put(PARAM_MESSAGES, listMessage);
			parameters.put(PARAM_READER, reader);

            return RESULT_NEXT;
        } catch (IOException e) {
            processLog.error("Failed open stream", e);
        }

        return RESULT_ERROR;

    }

    /**
	 * Open source registry file
	 *
	 * @param spFile Registry file
	 * @return FileSource
	 * @throws IOException if failure occurs
	 */
	private FileSource openRegistryFile(FPFile spFile) throws IOException {
		return spFile.toFileSource();
	}

    @Required
    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }

	public void setMinReadChars(Long minReadChars) {
		this.minReadChars = minReadChars;
	}
}
