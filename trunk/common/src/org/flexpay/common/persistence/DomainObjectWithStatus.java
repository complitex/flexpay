package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

public class DomainObjectWithStatus extends DomainObject implements ObjectWithStatus {

	protected int status = ObjectWithStatus.STATUS_ACTIVE;

	public DomainObjectWithStatus() {
	}

	public DomainObjectWithStatus(@NotNull Long id) {
		super(id);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void disable() {
		status = STATUS_DISABLED;
	}

	public void activate() {
		status = STATUS_ACTIVE;
	}

	public boolean isActive() {
		return status == STATUS_ACTIVE;
	}

	public boolean isNotActive() {
		return status == STATUS_DISABLED;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", status).
				toString();
	}

}
