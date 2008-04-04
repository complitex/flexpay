package org.flexpay.common.util.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;

import java.io.File;
import java.util.*;

public class ApplicationConfig {

	private static ApplicationConfig instance;

	private List<Language> languages = new ArrayList<Language>(3);
	public static final String USER_PREFERENCES_SESSION_ATTRIBUTE = "FLEXPAY_USER_PREFERENCES_SESSION_ATTRIBUTE";

	private static final Date DATE_PAST_INFINITE = new GregorianCalendar(1900, 0, 1).getTime();
	private static final Date DATE_FUTURE_INFINITE = new GregorianCalendar(2100, 11, 31).getTime();

	private File dataRoot;

	private File eircDataRoot;
	private File eircKvitPattern;
	private String eircId;

	private File szDataRoot;
	private String szDefaultDbfFileEncoding;

	private String testProp;

	public Date getPastInfinite() {
		return DATE_PAST_INFINITE;
	}

	public Date getFutureInfinite() {
		return DATE_FUTURE_INFINITE;
	}

	/**
	 * Getter for property 'instance'.
	 *
	 * @return Value for property 'instance'.
	 */
	public static ApplicationConfig getInstance() {
		if (instance == null) {
			return new ApplicationConfig();
		}
		return instance;
	}

	/**
	 * Do not instantiate ApplicationConfig.
	 */
	protected ApplicationConfig() {
	}

	/**
	 * Getter for property 'languages'.
	 *
	 * @return Value for property 'languages'.
	 */
	public List<Language> getLanguages() {
		return Collections.unmodifiableList(languages);
	}

	public void addLanguage(Language language) {
		languages.add(language);
	}

	/**
	 * Setter for property 'languages'.
	 *
	 * @param languages Value to set for property 'languages'.
	 */
	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	/**
	 * Get Default Language configuaration option
	 *
	 * @return Language
	 * @throws FlexPayException if Default language is not configured
	 */
	public Language getDefaultLanguage() throws FlexPayException {
		for (Language language : languages) {
			if (language.isDefault()) {
				return language;
			}
		}
		throw new FlexPayException("No default language defined");
	}

	/**
	 * Setter for property 'instance'.
	 *
	 * @param config Value to set for property 'instance'.
	 */
	static void setInstance(ApplicationConfig config) {
		instance = config;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("languages", languages.toArray()).toString();
	}

	public File getDataRoot() {
		return dataRoot;
	}

	public void setDataRoot(String dataRoot) {
		this.dataRoot = new File(dataRoot);
		if (!this.dataRoot.exists()) {
			this.dataRoot.mkdirs();
		}
	}

	public File getSzDataRoot() {
		return szDataRoot;
	}

	public void setSzDataRoot(String szDataRoot) {
		this.szDataRoot = new File(dataRoot, szDataRoot);
		if (!this.szDataRoot.exists()) {
			this.szDataRoot.mkdirs();
		}
	}

	public String getSzDefaultDbfFileEncoding() {
		return szDefaultDbfFileEncoding;
	}

	public void setSzDefaultDbfFileEncoding(String szDefaultDbfFileEncoding) {
		this.szDefaultDbfFileEncoding = szDefaultDbfFileEncoding;
	}

	public String getEircId() {
		return eircId;
	}

	public void setEircId(String eircId) {
		this.eircId = eircId;
	}

	public File getEircDataRoot() {
		return eircDataRoot;
	}

	public void setEircDataRoot(String eircDataRoot) {
		this.eircDataRoot = new File(dataRoot, eircDataRoot);
		if (!this.eircDataRoot.exists()) {
			this.eircDataRoot.mkdirs();
		}
	}

	public String getTestProp() {
		return testProp;
	}

	public void setTestProp(String prop) {
		this.testProp = prop;
	}

	/**
	 * @return the eircKvitPattern
	 */
	public File getEircKvitPattern() {
		return eircKvitPattern;
	}

	/**
	 * @param eircKvitPattern the eircKvitPattern to set
	 */
	public void setEircKvitPattern(String eircKvitPattern) {
		this.eircKvitPattern = new File(eircDataRoot, eircKvitPattern);
	}
}
