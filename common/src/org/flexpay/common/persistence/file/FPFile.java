package org.flexpay.common.persistence.file;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.FPModule;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.FileSource;
import org.flexpay.common.util.StringUtil;
import org.flexpay.common.util.io.InputStreamCallback;
import org.flexpay.common.util.io.OutputStreamCallback;
import org.flexpay.common.util.io.ReaderCallback;
import org.flexpay.common.util.io.WriterCallback;
import org.jetbrains.annotations.NotNull;

import javax.activation.DataSource;
import java.io.*;
import java.util.Date;

public class FPFile extends DomainObject implements DataSource {

	private String nameOnServer;
	private String originalName;
	private String userName;
	private String description;
	private Long size = 0L;
	private Date creationDate = new Date();
	private FPModule module;

	public String getNameOnServer() {
		return nameOnServer;
	}

	public void setNameOnServer(String nameOnServer) {
		this.nameOnServer = nameOnServer;
	}

	public String getOriginalName() {
		return originalName;
	}

	public String getGetFileNameWithoutExtension() {
		return StringUtil.getFileNameWithoutExtension(originalName);
	}

	public String getExtension() {
		return StringUtil.getFileExtension(originalName);
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public FPModule getModule() {
		return module;
	}

	public void setModule(@NotNull FPModule module) {
		this.module = module;
	}

	/**
	 * Create output stream for this file
	 *
	 * @return BufferedOutputStream
	 * @throws IOException if stream open fails
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		return new BufferedOutputStream(new FileOutputStream(getFileOnServer())) {
			@Override
			public void close() throws IOException {
				super.close();
				updateSize();
			}
		};
	}

	/**
	 * Create input stream for this file
	 *
	 * @return BufferedInputStream
	 * @throws IOException if stream open fails
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(new FileInputStream(getFileOnServer()));
	}

	@Override
	public String getContentType() {
		return "application/octet-stream";
	}

	@Override
	public String getName() {
		return getOriginalName();
	}

	public void updateSize() {
		this.size = getFileOnServer().length();
	}

	public void withInputStream(InputStreamCallback callback) throws IOException {

		InputStream is = getInputStream();
		try {
			callback.read(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public void withOutputStream(OutputStreamCallback callback) throws IOException {

		OutputStream os = getOutputStream();
		try {
			callback.write(os);
		} finally {
			IOUtils.closeQuietly(os);
			updateSize();
		}
	}

	public void withReader(String encoding, ReaderCallback callback) throws IOException {

		@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
		Reader r = new InputStreamReader(getInputStream(), encoding);
		try {
			callback.read(r);
		} finally {
			IOUtils.closeQuietly(r);
		}
	}

	public void withWriter(String encoding, WriterCallback callback) throws IOException {

		@SuppressWarnings ({"IOResourceOpenedButNotSafelyClosed"})
		Writer w = new OutputStreamWriter(getOutputStream(), encoding);
		try {
			callback.write(w);
		} finally {
			IOUtils.closeQuietly(w);
			updateSize();
		}
	}

	/**
	 * Create source file from this file
	 *
	 * @return FileSource
	 * @throws IOException if failure occurs
	 */
	public FileSource toFileSource() throws IOException {
		File file = getFileOnServer();
		if (file == null) {
			throw new FileNotFoundException("For FPFile(id=" + getId()
											+ ") not found temp file: " + getNameOnServer());
		}

		String type = "";
		if (getOriginalName().endsWith(".zip")) {
			type = "zip";
		} else if (getOriginalName().endsWith(".gz")) {
			type = "gzip";
		}
		return new FileSource(file, type);
	}

	/**
	 * Returns file
	 *
	 * @return file on file system
	 */
	private File getFileOnServer() {
		if (this.getNameOnServer() == null) {
			return null;
		}
		return new File(FPFileUtil.getFileLocalPath(this));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("nameOnServer", nameOnServer).
				append("originalName", originalName).
				append("userName", userName).
				append("size", size).
				append("creationDate", creationDate).
				toString();
	}

}
