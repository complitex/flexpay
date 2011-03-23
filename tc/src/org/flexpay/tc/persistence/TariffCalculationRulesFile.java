package org.flexpay.tc.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.file.FPFileType;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class TariffCalculationRulesFile extends DomainObjectWithStatus {

	public static final Long TARIFF_CALCULATION_RULES_FILE_TYPE = 1L;

	private Date creationDate = new Date();
	private String userName;
	private FPFileType type;
	private FPFile file;
	private Set<TariffCalculationRulesFileTranslation> translations = Collections.emptySet();

	public TariffCalculationRulesFile() {
	}

	public TariffCalculationRulesFile(@NotNull Long id) {
		super(id);
	}

	public TariffCalculationRulesFile(@NotNull Stub<TariffCalculationRulesFile> stub) {
		super(stub.getId());
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public FPFileType getType() {
		return type;
	}

	public void setType(FPFileType type) {
		this.type = type;
	}

	public FPFile getFile() {
		return file;
	}

	public void setFile(FPFile file) {
		this.file = file;
	}

	public Set<TariffCalculationRulesFileTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<TariffCalculationRulesFileTranslation> translations) {
		this.translations = translations;
	}

	public void setTranslation(@NotNull TariffCalculationRulesFileTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("creationDate", creationDate).
				append("userName", userName).
				append("type", type).
				append("file", file).
				toString();
	}

}
