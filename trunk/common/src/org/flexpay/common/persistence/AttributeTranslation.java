package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

public class AttributeTranslation extends Translation {

	private String value;

	/**
	 * Constructs a new Translation.
	 */
	public AttributeTranslation() {
	}

	public AttributeTranslation(@NotNull String name, @NotNull  Language lang, @NotNull  String value) {
		super(name, lang);
		this.value = value;
	}

	/**
	 * Getter for property 'value'.
	 *
	 * @return Value for property 'value'.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter for property 'value'.
	 *
	 * @param value Value to set for property 'value'.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "[" + getName() + ":" + value + "," + getLang() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AttributeTranslation)) {
			return false;
		}
		final AttributeTranslation that = (AttributeTranslation) o;

		return new EqualsBuilder()
				.append(value, that.getValue())
				.appendSuper(super.equals(o))
				.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(value)
				.appendSuper(super.hashCode())
				.hashCode();
	}
}
