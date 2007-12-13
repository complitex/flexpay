package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

public class Translation implements Serializable {
    private String name;
    private Language lang;

    /** Constructs a new Translation. */
	public Translation() {
    }

    public Translation(String name, Language lang) {
        this.name = name;
        this.lang = lang;
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
	 * Getter for property 'lang'.
	 *
	 * @return Value for property 'lang'.
	 */
	public Language getLang() {
        return lang;
    }

    /**
	 * Setter for property 'name'.
	 *
	 * @param name Value to set for property 'name'.
	 */
	public void setName(String name) {
        this.name = name;
    }

    /**
	 * Setter for property 'lang'.
	 *
	 * @param lang Value to set for property 'lang'.
	 */
	public void setLang(Language lang) {
        this.lang = lang;
    }

    /** {@inheritDoc} */
	public String toString() {
        return name + ";" + lang;
    }

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(lang)
				.append(name)
				.toHashCode();
	}

	/** {@inheritDoc} */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (! (o instanceof Translation)) {
			return false;
		}
		final Translation that = (Translation) o;

		return new EqualsBuilder()
				.append(lang, that.getLang())
				.append(name, that.getName())
				.isEquals();
	}

}


