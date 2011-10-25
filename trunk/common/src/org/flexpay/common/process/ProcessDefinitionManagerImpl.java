package org.flexpay.common.process;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.drools.builder.*;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.process.Process;
import org.drools.io.ResourceFactory;
import org.drools.util.codec.Base64;
import org.flexpay.common.process.dao.ProcessDefinitionJbpmDao;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.persistence.ProcessDefinition;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.SAXException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class ProcessDefinitionManagerImpl implements ProcessDefinitionManager {

	private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionManagerImpl.class);

	private static final String PROCESS_FILE_EXTENTION = "bpmn";

	private ProcessDefinitionJbpmDao delegate;

	/**
	 * singleton instance
	 */
	private static final ProcessDefinitionManagerImpl instance = new ProcessDefinitionManagerImpl();

	/**
	 * Predefined set of paths where to look definitions if not already deployed
	 */
	private final List<String> definitionPaths = CollectionUtils.list();

	private String guvnorUrl;
	private String guvnorPackageName;
	private String guvnorUserName;
	private String guvnorUserPassword;

	public static volatile boolean PROCESS_DEFINITIONS_LOADED = false;

	/**
	 * protected constructor
	 */
	private ProcessDefinitionManagerImpl() {
		log.debug("ProcessDefinitionManager constructor called");
	}

	public static ProcessDefinitionManagerImpl getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	@Override
	public List<ProcessDefinition> getProcessDefinitions() {
		List<org.drools.definition.process.Process> processes = delegate.getProcesses();
		List<ProcessDefinition> result = new ArrayList<ProcessDefinition>();
		for (org.drools.definition.process.Process process : processes) {
			result.add(processDefinition(process));
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	@Override
	public ProcessDefinition getProcessDefinition(@NotNull String definitionId) {
		return processDefinition(delegate.getProcess(definitionId));
	}

	public List<ProcessDefinition> removeProcessDefinition(String definitionId) {
		//delegate.removeProcess(definitionId);
		return getProcessDefinitions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deployProcessDefinitions() throws ProcessDefinitionException {
		try {
			processingProcessDefinition(getProcessesDefinitionContent(), true);
		} catch (IOException e) {
			throw new ProcessDefinitionException("Can not get process definition content", e,
							"common.error.pm.pd_deployment_failed");
		} catch (SAXException e) {
			throw new ProcessDefinitionException("Process definition deployment failed to knowledge base", e,
					"common.error.pm.pd_deployment_failed");
		}
		for (String path : definitionPaths) {
			log.debug("Looking up {}", path);
			File file = ApplicationConfig.getResourceAsFile(path);
			File[] definitions;
			if (file != null) {
				if (file.isDirectory()) {
					definitions = file.listFiles((FilenameFilter) new SuffixFileFilter(PROCESS_FILE_EXTENTION));
				} else {
					definitions = new File[]{file};
				}
				deployProcessDefinition(definitions, false, true);
			}
		}
		PROCESS_DEFINITIONS_LOADED = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deployProcessDefinition(@NotNull String name) throws ProcessDefinitionException {
		deployProcessDefinition(name, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void deployProcessDefinition(@NotNull String name, boolean replace) throws ProcessDefinitionException {
		// Search process in package
		if (!replace) {
			ProcessDefinition definition = getProcessDefinition(name);
			long processVersion = getPackageContentProcessDefinitionVersion(name);
			if (definition != null) {
				log.debug("Process definition version is {} in the base knowledge. Process definition version is {} in the storage.", definition.getVersion(), processVersion);
			} else {
				log.debug("Process definition {} did not find", name);
			}
			if (definition != null && definition.getVersion() >= processVersion) {
				log.debug("Did not deploy process definition {}. It content in knowledge session. Version {}", name, definition.getVersion());
				return;
			}
			if (processVersion >= 0) {
				try {
					processingProcessDefinition(list(getProcessDefinitionContent(name + "." + PROCESS_FILE_EXTENTION)), false);
				} catch (IOException e) {
					throw new ProcessDefinitionException("Process definition deployment failed " + name, e,
							"common.error.pm.pd_deployment_failed", name);
				} catch (SAXException e) {
					throw new ProcessDefinitionException("Process definition deployment failed " + name, e,
							"common.error.pm.pd_deployment_failed", name);
				}
				log.debug("Did not deploy process definition {}. It content in storage. Version {}", name, processVersion);
				return;
			}
		}
		log.debug("Requested definition deployment: {}", name);
		for (String path : definitionPaths) {
			String resource = path + "/" + name + "." + PROCESS_FILE_EXTENTION;
			log.debug("Looking up {}", resource);
			File file = ApplicationConfig.getResourceAsFile(resource);
			if (file != null) {
				log.debug("Found!");
				deployProcessDefinition(file);
				return;
			}
		}

		log.warn("No definition found: {}", name);
		throw new ProcessDefinitionException("Process definition for name " + name + " file did not find!",
				"common.error.pm.pd_file_not_found", name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deployProcessDefinition(@NotNull File definitionFile) throws ProcessDefinitionException {
		deployProcessDefinition(new File[]{definitionFile}, true, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deployProcessDefinition(String name, long version) throws ProcessDefinitionException {
		String fileName = name + "." + PROCESS_FILE_EXTENTION;
		try {
			InputStream is = getPackageProcessContent(guvnorPackageName, name, Long.toString(version));
			if (is == null) {
				throw new ProcessDefinitionException("Process definition for name " + fileName +" file not found in storage!",
						"common.error.pm.pd_file_not_found_in_storage", fileName);
			}
			ProcessDefinition definition = new ProcessDefinition(name, name, version);
			definition.setDeploymentId(fileName);
			processingProcessDefinition(list(new ProcessDefinitionContent(definition, is)), false);
		} catch (IOException e) {
			throw new ProcessDefinitionException("Can not get process definition content", e,
							"common.error.pm.pd_deployment_failed", name);
		} catch (SAXException e) {
			throw new ProcessDefinitionException("Process definition deployment failed " + name, e,
					"common.error.pm.pd_deployment_failed", name);
		}
	}

	private void deployProcessDefinition(@NotNull File[] definitionFiles, boolean forceAddFileToStorage, boolean ignoreExceptions) throws ProcessDefinitionException {
		List<ProcessDefinitionContent> contents = list();
		for (File definitionFile : definitionFiles) {
			try {
				try {
					if (forceAddFileToStorage || !isPackageContentProcessDefinition(StringUtils.removeEnd(definitionFile.getName(), "." + PROCESS_FILE_EXTENTION))) {
						if (!addFileToStorage(definitionFile)) {
							throw new ProcessDefinitionException("Process definition deployment failed to storage",
									"common.error.pm.pd_deployment_failed_to_storage", definitionFile.getName());
						}
						if (!buildPackage(guvnorPackageName)) {
							deleteFile(definitionFile.getName());
						}
						contents.add(getProcessDefinitionContent(definitionFile.getName()));
					}
				} catch (IOException e) {
					throw new ProcessDefinitionException("Process definition deployment failed " + definitionFile.getName(), e,
							"common.error.pm.pd_deployment_failed", definitionFile.getName());
				}
			} catch (ProcessDefinitionException e) {
				if (ignoreExceptions) {
					log.warn("Ignore exception for definition " + definitionFile.getName(), e);
				} else {
					throw e;
				}
			}
		}
		try {
			processingProcessDefinition(contents, ignoreExceptions);
		} catch (IOException e) {
			throw new ProcessDefinitionException("Process definition deployment failed " + ArrayUtils.toString(definitionFiles), e,
					"common.error.pm.pd_deployment_failed", ArrayUtils.toString(definitionFiles));
		} catch (SAXException e) {
			throw new ProcessDefinitionException("Process definition deployment failed " + ArrayUtils.toString(definitionFiles), e,
					"common.error.pm.pd_deployment_failed", ArrayUtils.toString(definitionFiles));
		}
	}

	private long getPackageContentProcessDefinitionVersion(String name) throws ProcessDefinitionException {
		try {
			log.debug("Get version for definition process '{}'", name);
			List<ProcessDefinition> definitions = getPackageContent(guvnorPackageName);
			for (ProcessDefinition definition : definitions) {
				log.debug("Definition: {} ({})", definition.getId(), definition.getVersion());
				if (StringUtils.equals(name, definition.getId())) {
					log.debug("Found definition process '{}' with version {}", definition.getId(), definition.getVersion());
					return definition.getVersion();
				}
			}
		} catch (IOException e) {
			throw new ProcessDefinitionException("Did not get package content ({})", name, e);
		}
		return -1;
	}

	private boolean isPackageContentProcessDefinition(String name) throws ProcessDefinitionException {
		return getPackageContentProcessDefinitionVersion(name) >= 0;
	}

	@NotNull
	private ProcessDefinitionContent getProcessDefinitionContent(String processDefinitionFileName) throws IOException, ProcessDefinitionException {
		List<ProcessDefinition> guvnorProcesses = getPackageContent(guvnorPackageName);
		ProcessDefinition resultGuvnorProcess = null;
		for (ProcessDefinition guvnorProcess : guvnorProcesses) {
			if (StringUtils.equals(processDefinitionFileName, guvnorProcess.getDeploymentId())) {
				resultGuvnorProcess = guvnorProcess;
				break;
			}
		}
		if (resultGuvnorProcess == null) {
			throw new ProcessDefinitionException("Process did not find in storage",
					"common.error.pm.pd_file_not_found_in_storage", processDefinitionFileName);
		}
		return new ProcessDefinitionContent(resultGuvnorProcess,
				getPackageProcessContent(guvnorPackageName, processDefinitionFileName, Long.toString(resultGuvnorProcess.getVersion())));
	}

	@NotNull
	private List<ProcessDefinitionContent> getProcessesDefinitionContent() throws IOException {
		List<ProcessDefinition> guvnorProcesses = getPackageContent(guvnorPackageName);
		List<ProcessDefinitionContent> contents = list();
		for (ProcessDefinition guvnorProcess : guvnorProcesses) {
			contents.add(new ProcessDefinitionContent(guvnorProcess,
				getPackageProcessContent(guvnorPackageName, guvnorProcess.getDeploymentId(), Long.toString(guvnorProcess.getVersion()))));
		}
		return contents;
	}

	private boolean addFileToStorage(@NotNull File definitionFile) throws ProcessDefinitionException, IOException {
		return isPackageContentProcessDefinition(StringUtils.removeEnd(definitionFile.getName(), "." + PROCESS_FILE_EXTENTION))? putFile(definitionFile): postFile(definitionFile);
	}

	private boolean postFile(@NotNull File file) throws IOException {
		FileEntity fileEntity = new FileEntity(file, "text/plain");

		HttpPost httppost = new HttpPost(builUri("org.drools.guvnor.Guvnor/api/packages/" + guvnorPackageName + "/" + file.getName()));
		httppost.setEntity(fileEntity);
		httppost.setHeader("Authorization", getAuthorizationString());

		org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response = httpclient.execute(httppost);
		log.debug("Post file StatusLine: {}", response.getStatusLine());

		return response.getStatusLine().getStatusCode() == 200;
	}

	private boolean putFile(@NotNull File file) throws IOException {
		FileEntity fileEntity = new FileEntity(file, "text/plain");

		HttpPut httpput = new HttpPut(builUri("org.drools.guvnor.Guvnor/api/packages/" + guvnorPackageName + "/" + file.getName()));
		httpput.setEntity(fileEntity);
		httpput.setHeader("Authorization", getAuthorizationString());

		org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response = httpclient.execute(httpput);
		log.debug("Put file StatusLine: {}", response.getStatusLine());

		return response.getStatusLine().getStatusCode() == 200;
	}

	private boolean deleteFile(String fileName) throws IOException {
		HttpDelete httpDelete = new HttpDelete(builUri("org.drools.guvnor.Guvnor/api/packages/" + guvnorPackageName + "/" + fileName));
		httpDelete.setHeader("Authorization", getAuthorizationString());

		org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response = httpclient.execute(httpDelete);
		log.debug("Delete file StatusLine: {}", response.getStatusLine());

		return response.getStatusLine().getStatusCode() == 200;
	}

	private boolean buildPackage(String packageName) throws IOException {
		HttpPost httppost = new HttpPost(builUri("org.drools.guvnor.Guvnor/action/compile"));
		httppost.setHeader("Authorization", getAuthorizationString());

		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("package-name", packageName));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");

		httppost.setEntity(entity);

		org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response = httpclient.execute(httppost);
		log.debug("Build package StatusLine: {}", response.getStatusLine());

		return response.getStatusLine().getStatusCode() == 200;
	}

	private List<ProcessDefinition> getPackageContent(String packageName) throws IOException {
		List<ProcessDefinition> processDefinitions = list();
		HttpGet httpGet = new HttpGet(builUri("org.drools.guvnor.Guvnor/api/packages/" + packageName));
		httpGet.setHeader("Authorization", getAuthorizationString());

		org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response = httpclient.execute(httpGet);
		log.debug("Get package '{}' content StatusLine: {}", packageName, response.getStatusLine());
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException(packageName + " " + response.getStatusLine().getReasonPhrase());
		}

		BufferedReader r = null;

		try {
			r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line;
			SimpleDateFormat sdf = getISODateFormat();
			while((line = r.readLine()) != null) {
				String[] splitResult = StringUtils.split(line, "=");
				if (splitResult.length == 2 && StringUtils.endsWith(splitResult[0], "." + PROCESS_FILE_EXTENTION)) {
					String processName = StringUtils.removeEnd(splitResult[0], "." + PROCESS_FILE_EXTENTION);
					String[] processInfo = StringUtils.split(splitResult[1], ",");
					if (processInfo.length == 2) {
						Date processLastModified;
						try {
							processLastModified = sdf.parse(processInfo[0]);
						} catch (ParseException e) {
							log.debug("Failed last modified date '{}' of process '{}'", new Object[]{processInfo[0], processName}, e);
							continue;
						}
						Long processVersion;
						try {
							processVersion = Long.parseLong(processInfo[1]);
						} catch (NumberFormatException e) {
							log.debug("Failed process version '{}' of process '{}'", new Object[]{processInfo[1], processName}, e);
							continue;
						}
						ProcessDefinition definition = new ProcessDefinition(processName, processName, processVersion);
						definition.setDeploymentId(splitResult[0]);
						definition.setLastModified(processLastModified);
						definition.setPackageName(packageName);
						processDefinitions.add(definition);
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(r);
		}
		return processDefinitions;
	}

	private InputStream getPackageProcessContent(String packageName, String processFileName, String version) throws IOException {
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append("org.drools.guvnor.Guvnor/api/packages/")
				  .append(packageName)
				  .append("/")
				  .append(processFileName);
		if (StringUtils.isNotEmpty(version)) {
			uriBuilder.append("?version=")
					  .append(version);
		}
		HttpGet httpGet = new HttpGet(builUri(uriBuilder.toString()));
		httpGet.setHeader("Authorization", getAuthorizationString());

		org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();

		HttpResponse response = httpclient.execute(httpGet);
		log.debug("Read package`s process ({}, {}, {}) StatusLine: {}", new Object[]{packageName, processFileName, version, response.getStatusLine()});
		if (response.getStatusLine().getStatusCode() != 200) {
			return null;
		}

		return response.getEntity().getContent();

		/*
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line;
			int i = 0;
			while((line = r.readLine()) != null) {
				System.out.println(++i + ":" +line);
			}
		} finally {
			IOUtils.closeQuietly(r);
		}
		*/

	}

	private void processingProcessDefinition(List<ProcessDefinitionContent> processesContent, boolean ignoreException) throws IOException, SAXException {
		KnowledgeBuilderConfiguration conf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		((PackageBuilderConfiguration) conf).initSemanticModules();
		((PackageBuilderConfiguration) conf).addSemanticModule(new BPMNSemanticModule());
		((PackageBuilderConfiguration) conf).addSemanticModule(new BPMNDISemanticModule());
		((PackageBuilderConfiguration) conf).addSemanticModule(new BPMNExtensionsSemanticModule());
		//XmlProcessReader processReader = new XmlProcessReader(
	    //    ((PackageBuilderConfiguration) conf).getSemanticModules());
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(conf);
		for (ProcessDefinitionContent processContent : processesContent) {
			try {
				processContent.processing(kbuilder);
			} catch (SAXException e) {
				if (ignoreException) {
					log.warn("Ignore exception while processing process definition " + processContent, e);
					continue;
				}
				throw e;
			}
			if (!kbuilder.getErrors().isEmpty()) {
				for (KnowledgeBuilderError error: kbuilder.getErrors()) {
					if (ignoreException) {
						log.warn(error.toString());
					} else {
						log.error(error.toString());
					}
				}
				if (!ignoreException) {
					throw new IllegalArgumentException("Errors while parsing knowledge base");
				}
			}
		}
		log.debug("delegate.addPackages");
		delegate.addPackages(kbuilder.getKnowledgePackages());
	}

	private String builUri(String end) {
		String resultUri = guvnorUrl + end;
		log.debug("Request: {}", resultUri);
		return resultUri;
	}

	private String getAuthorizationString() {
		return "BASIC " + new String(Base64.encodeBase64((guvnorUserName + ":" + guvnorUserPassword).getBytes()));
	}

	private String getProcessDefininitionNameFromFile(@NotNull File definitionFile) {
		return StringUtils.removeEnd(definitionFile.getName(), "." + PROCESS_FILE_EXTENTION);
	}

	/**
	 * This is the format used to sent dates as text, always.
	 * @return Date format in pattern "yyyy-MM-dd'T'HH:mm:ss"
	 */
	public static SimpleDateFormat getISODateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}

	private static ProcessDefinition processDefinition(org.drools.definition.process.Process process) {
		if (process == null) {
			return null;
		}
		long version = 0;
		try {
			version = new Long(process.getVersion());
		} catch (NumberFormatException e) {
			// Do nothing, keep version 0
		}
		ProcessDefinition result = new ProcessDefinition(
				process.getId(), process.getName(), version);
		result.setPackageName(process.getPackageName());
		result.setDeploymentId("N/A");
		return result;
	}

	private class ProcessDefinitionContent {
		private ProcessDefinition processDefinition;
		private InputStream content;

		private ProcessDefinitionContent(ProcessDefinition processDefinition, InputStream content) {
			this.processDefinition = processDefinition;
			this.content = content;
		}

		public void processing(KnowledgeBuilder kbuilder) throws IOException, SAXException {
			log.debug("Processing process definition: {} ({})", processDefinition.getName(), processDefinition.getVersion());
			kbuilder.add(ResourceFactory.newReaderResource(new InputStreamReader(content)), ResourceType.BPMN2);
			for (KnowledgePackage knowledgePackage : kbuilder.getKnowledgePackages()) {
				for (Process process : knowledgePackage.getProcesses()) {
					if (StringUtils.equals(process.getId(), processDefinition.getId())) {
						((org.jbpm.process.core.impl.ProcessImpl)process).setVersion(Long.toString(processDefinition.getVersion()));
						log.debug("Set process definition version: {} ({})", processDefinition.getId(), processDefinition.getVersion());
						return;
					}
				}
			}
			/*
			List<Process> processes = processReader.read(content);
			for (Process p : processes) {
				RuleFlowProcess ruleFlowProcess = (RuleFlowProcess)p;
				ruleFlowProcess.setVersion(Long.toString(processDefinition.getVersion()));
				log.debug("Processing content: name={}, version={}", ruleFlowProcess.getName(), ruleFlowProcess.getVersion());
				kbuilder.add(ResourceFactory.newReaderResource(
						new StringReader(XmlBPMNProcessDumper.INSTANCE.dump(ruleFlowProcess))), ResourceType.BPMN2);
			}
			*/
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("processDefinition", processDefinition).toString();
		}
	}

	@Required
	public void setDelegate(ProcessDefinitionJbpmDao delegate) {
		this.delegate = delegate;
	}

	@Required
	public void setGuvnorUrl(String guvnorUrl) {
		if (!guvnorUrl.endsWith("/")) {
			guvnorUrl += "/";
		}
		this.guvnorUrl = guvnorUrl;
	}

	@Required
	public void setGuvnorPackageName(String guvnorPackageName) {
		this.guvnorPackageName = guvnorPackageName;
	}

	@Required
	public void setGuvnorUserName(String guvnorUserName) {
		this.guvnorUserName = guvnorUserName;
	}

	@Required
	public void setGuvnorUserPassword(String guvnorUserPassword) {
		this.guvnorUserPassword = guvnorUserPassword;
	}

	@Override
	public void setDefinitionPaths(List<String> definitionPaths) {
		this.definitionPaths.clear();
		this.definitionPaths.addAll(definitionPaths);
	}
}
