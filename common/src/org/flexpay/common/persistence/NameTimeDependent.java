package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * Base class for objects that has time-dependent name
 */
public class NameTimeDependent<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
		extends DomainObjectWithStatus {

	private TimeLine<T, DI> namesTimeLine;

	/**
	 * Getter for property 'namesTimeLine'.
	 *
	 * @return Value for property 'namesTimeLine'.
	 */
	public TimeLine<T, DI> getNamesTimeLine() {
		return namesTimeLine;
	}

	/**
	 * Setter for property 'namesTimeLine'.
	 *
	 * @param namesTimeLine Value to set for property 'namesTimeLine'.
	 */
	public void setNamesTimeLine(TimeLine<T, DI> namesTimeLine) {
		this.namesTimeLine = namesTimeLine;
	}

	/**
	 * Setter for property 'nameTemporals'.
	 *
	 * @param nameTemporals Value to set for property 'nameTemporals'.
	 */
	public void setNameTemporals(List<DI> nameTemporals) {
		namesTimeLine = new TimeLine<T, DI>(nameTemporals);
	}

	/**
	 * Getter for property 'nameTemporals'.
	 *
	 * @return Value for property 'nameTemporals'.
	 */
	public List<DI> getNameTemporals() {
		return namesTimeLine.getIntervals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NameTimeDependent)) {
			return false;
		}

		NameTimeDependent that = (NameTimeDependent) obj;

		return new EqualsBuilder()
				.append(namesTimeLine, that.namesTimeLine)
				.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(namesTimeLine)
				.toHashCode();
	}
}
