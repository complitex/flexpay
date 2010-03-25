package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

public class UserRoleTranslation extends Translation {

	public UserRoleTranslation() {
	}

	public UserRoleTranslation(@NotNull String name) {
		super(name, getDefaultLanguage());
	}

	public UserRoleTranslation(@NotNull String name, @NotNull Language lang) {
		super(name, lang);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserRoleTranslation)) {
			return false;
		}

		UserRoleTranslation that = (UserRoleTranslation) obj;
		return new EqualsBuilder()
				.appendSuper(super.equals(that))
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append("id", getId())
				.append("language", getLang().getLangIsoCode())
				.append("name", getName())
				.toString();
	}

}

