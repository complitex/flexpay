package org.flexpay.eirc.actions;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.*;
import java.util.Map;

public class SpFileAction extends FPActionSupport {

	private Long spFileId;
	private Long processId = null;
	@NonNls
	private String action;

	private FPFileService fpFileService;
	private ProcessManager processManager;

	@NotNull
	public String doExecute() throws Exception {

		if (spFileId == null || spFileId <= 0) {
			throw new FlexPayException("Invalid registry id: " + spFileId);
		}

		if ("loadToDb".equals(action)) {
			Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
			contextVariables.put("FileId", spFileId);
			contextVariables.put("FileType", getFileType(spFileId));
			processId = processManager.createProcess("ParseRegistryProcess", contextVariables);
			log.debug("Load to db process id {}", processId);
			if (processId == null) {
				throw new Exception("Failed creating process, unknown reason");
			}

			addActionError(getText("eirc.registry.parse_started"));
		} else if ("loadFromDb".equals(action)) {
			// SzFileUtil.loadFromDb(szFile);
		} else if ("deleteFromDb".equals(action)) {
//			 SzFileUtil.deleteRecords(szFile);
		} else if ("fullDelete".equals(action)) {
			// SzFileUtil.delete(szFile);
		}

		return REDIRECT_SUCCESS;
	}

	private String getFileType(Long fileId) throws FlexPayException {

		File file = fpFileService.getFileFromFileSystem(new Stub<FPFile>(fileId));

		String firstMbFileString =
				"                                                                                                    " +
				"                                                                                                    " +
				"                                                                                                    ";

		String line = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Cp866"), 500);
			line = reader.readLine();

			if (firstMbFileString.equals(line)) {
				line = reader.readLine();

				String[] fields = line.split("=");
				if (fields.length == 3) {
					return "mbCorrections";
				} else if (fields.length == 4) {
					return "mbRegistry";
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return "registry";
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public void setSpFileId(Long spFileId) {
		this.spFileId = spFileId;
	}

	public void setAction(@NonNls String action) {
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

}
