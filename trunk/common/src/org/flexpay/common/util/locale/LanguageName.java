package org.flexpay.common.util.locale;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Locale;

/**
 * Language name translation to particular locale
 */
public class LanguageName {
	private Locale locale;
	private String name;

	/** Constructs a new LanguageName. */
	public LanguageName() {
	}

	public LanguageName(Locale locale, String name) {
		this.locale = locale;
		this.name = name;
	}

	/**
	 * Getter for property 'locale'.
	 *
	 * @return Value for property 'locale'.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Setter for property 'locale'.
	 *
	 * @param locale Value to set for property 'locale'.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Setter for property 'locale'.
	 *
	 * @param locale Value to set for property 'locale'.
	 */
	public void setLocaleName(String locale) {
		this.locale = new Locale(locale);
	}

	public String getLocaleName() {
		return locale.getCountry();
	}

	/**
	 * Getter for property 'name'.
	 *
	 * @return Value for property 'name'.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for property 'name'.
	 *
	 * @param name Value to set for property 'name'.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Locale", locale.getDisplayName())
				.append("Name", name)
				.toString();
	}

}
