package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class UserRole extends DomainObjectWithStatus {

	private Set<UserRoleTranslation> translations = set();
	private String externalId;

	public UserRole() {
	}

	public UserRole(Long id) {
		super(id);
	}

	public UserRole(@NotNull Stub<UserRole> stub) {
		super(stub.getId());
	}

	public Set<UserRoleTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<UserRoleTranslation> translations) {
		this.translations = translations;
	}

	public void setTranslation(UserRoleTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof UserRole && super.equals(obj);

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("externalId", getExternalId()).
				toString();
	}

}
