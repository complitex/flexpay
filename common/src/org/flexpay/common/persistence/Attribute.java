package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Attribute
 */
public class Attribute extends DomainObject {
    private String name;
    private String value;

    /**
	 * Constructs a new Attribute.
	 */
    public Attribute() {
    }

    public Attribute(@NotNull String name, @NotNull String value) {
        this.name = name;
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
    public void setValue(@NotNull String value) {
        this.value = value;
    }

    /**
	 * Getter for property 'name'.
	 *
	 * @return Value for property 'name'.
	 */
	@NotNull
	public String getName() {
		return name != null ? name : "";
	}

    /**
	 * Setter for property 'name'.
	 *
	 * @param name Value to set for property 'name'.
	 */
	public void setName(@NotNull String name) {
		this.name = name;
	}

    /**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "[" + getName() + ":" + value + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Attribute)) {
			return false;
		}
		final Attribute that = (Attribute) o;

		return new EqualsBuilder()
				.append(value, that.getValue())
				.append(name, that.getName())
				.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(value)
				.append(name)
				.toHashCode();
	}
}
