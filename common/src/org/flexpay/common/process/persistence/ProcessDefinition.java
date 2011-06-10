package org.flexpay.common.process.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

public class ProcessDefinition {
	private String id;

	private String name;
	private long version;
	private String description;
	private String packageName;
	private Date lastModified;

	private String deploymentId;
	private boolean suspended;
	private String formUrl = null;
	private String diagramUrl = null;

	public ProcessDefinition() {
	}

	public ProcessDefinition(String id, String name, long version) {
		this.id = id;
		this.name = name;
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProcessDefinition that = (ProcessDefinition) o;

		if (version != that.version) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (int) (version ^ (version >>> 32));
		return result;
	}

	public void setFormUrl(String s) {
		this.formUrl = s;
	}

	public String getFormUrl() {
		return formUrl;
	}

	public String getDiagramUrl() {
		return diagramUrl;
	}

	public void setDiagramUrl(String diagramUrl) {
		this.diagramUrl = diagramUrl;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
