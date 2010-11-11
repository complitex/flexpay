package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Certificate extends DomainObject {

	private UserPreferences userPreferences;
	private String description;
	private Date beginDate;
	private Date endDate;
	private boolean blocked;

	public Certificate() {
	}

    public Certificate(@NotNull Long id) {
        setId(id);
    }

	public Certificate(UserPreferences preferences, String description, Date beginDate, Date endDate) {
		this.userPreferences = preferences;
		this.description = description;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.blocked = false;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isTimeUpdate() {
		Date nexMonthDate = DateUtils.addMonths(new Date(), 1);
		return nexMonthDate.after(getEndDate());
	}

	public boolean isExpired() {
		return new Date().after(getEndDate());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("version", version).
				append("userPreferences", userPreferences).
				append("description", description).
				append("beginDate", beginDate).
				append("endDate", endDate).
				toString();
	}
}
