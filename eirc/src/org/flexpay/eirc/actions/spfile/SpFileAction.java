package org.flexpay.eirc.actions.spfile;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.process.registry.FileParserJob;
import org.flexpay.eirc.process.registry.GetRegistryMessageActionHandler;
import org.flexpay.eirc.sp.FileParser;
import org.flexpay.eirc.sp.SpFileReader;
import org.flexpay.eirc.sp.impl.LineParser;
import org.flexpay.eirc.sp.impl.MbParsingConstants;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Map;

public class SpFileAction extends FPActionSupport {

	public final static String LOAD_TO_DB_ACTION = "loadToDb";
	public final static String LOAD_FROM_DB_ACTION = "loadFromDb";
	public final static String DELETE_FROM_DB_ACTION = "deleteFromDb";
	public final static String FULL_DELETE_ACTION = "fullDelete";

	private FPFile spFile = new FPFile();
	private Long processId = null;
	private String action;

	private FPFileService fpFileService;
	private ProcessManager processManager;
    private LineParser lineParser;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (spFile.isNew()) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		spFile = fpFileService.read(stub(spFile));
		if (spFile == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		if (LOAD_TO_DB_ACTION.equals(action)) {
            String fileType = getFileType(spFile);
            if (fileType == null) {
                addActionError(getText("common.error.file.unknown_type"));
                return REDIRECT_ERROR;
            }

			Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
			if (FileParser.REGISTRY_FILE_TYPE.equals(fileType)) {
				contextVariables.put(GetRegistryMessageActionHandler.PARAM_FILE_ID, spFile.getId());
				processId = processManager.createProcess("ParseFPRegistryProcess2", contextVariables);
			} else {
				contextVariables.put(FileParserJob.PARAM_FILE_ID, spFile.getId());
				contextVariables.put(FileParserJob.PARAM_FILE_TYPE, fileType);
				processId = processManager.createProcess("ParseRegistryProcess", contextVariables);
			}
			log.debug("Load to db process id {}", processId);
			if (processId == null) {
				throw new Exception("Failed creating process, unknown reason");
			}

			addActionMessage(getText("eirc.registry.parse_started"));
		} else if (LOAD_FROM_DB_ACTION.equals(action)) {
			// SzFileUtil.loadFromDb(szFile);
		} else if (DELETE_FROM_DB_ACTION.equals(action)) {
//			 SzFileUtil.deleteRecords(szFile);
		} else if (FULL_DELETE_ACTION.equals(action)) {
			// SzFileUtil.delete(szFile);
		}

		return REDIRECT_SUCCESS;
	}

    @Nullable
	private String getFileType(FPFile file) {

		BufferedReader reader = null;
		try {
			//noinspection IOResourceOpenedButNotSafelyClosed
			reader = new BufferedReader(new InputStreamReader(file.toFileSource().openStream(), MbParsingConstants.REGISTRY_FILE_ENCODING));
			String line = reader.readLine();

			if (MbParsingConstants.FIRST_FILE_STRING.equals(line)) {
				line = reader.readLine();

				String[] fields = lineParser.parse(line);
				if (fields.length == 3) {
					return FileParser.MB_CORRECTIONS_FILE_TYPE;
				} else if (fields.length == 4) {
					return FileParser.MB_REGISTRY_FILE_TYPE;
				}
			} else if (line.length() > 0 && line.codePointAt(0) == SpFileReader.Message.MESSAGE_TYPE_HEADER) {
                return FileParser.REGISTRY_FILE_TYPE;
            }

		} catch (IOException e) {
			log.error("Failed getting file type: " + file, e);
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return null;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public void setSpFile(FPFile spFile) {
		this.spFile = spFile;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getProcessId() {
		return processId;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

    @Required
    public void setLineParser(LineParser lineParser) {
        this.lineParser = lineParser;
    }

}
