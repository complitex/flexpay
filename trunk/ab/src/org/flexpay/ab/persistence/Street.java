package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.TranslationUtil;

import java.util.*;

/**
 * Street
 */
public class Street extends NameTimeDependentChild<StreetName, StreetNameTemporal> {

	private static Logger log = Logger.getLogger(Street.class);
	private static final SortedSet<StreetTypeTemporal> EMPTY_SORTED_SET =
			Collections.unmodifiableSortedSet(new TreeSet<StreetTypeTemporal>());

	private Set<District> districts = Collections.emptySet();
	private TimeLine<StreetType, StreetTypeTemporal> typesTimeLine;
	private Set<Buildings> buildingses = Collections.emptySet();

	public Street() {
	}

	public Street(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'districts'.
	 *
	 * @return Value for property 'districts'.
	 */
	public Set<District> getDistricts() {
		return districts;
	}

	/**
	 * Setter for property 'districts'.
	 *
	 * @param districts Value to set for property 'districts'.
	 */
	public void setDistricts(Set<District> districts) {
		this.districts = districts;
	}

	/**
	 * Getter for property 'buildingses'.
	 *
	 * @return Value for property 'buildingses'.
	 */
	public Set<Buildings> getBuildingses() {
		return buildingses;
	}

	/**
	 * Setter for property 'buildingses'.
	 *
	 * @param buildingses Value to set for property 'buildingses'.
	 */
	public void setBuildingses(Set<Buildings> buildingses) {
		this.buildingses = buildingses;
	}

	/**
	 * Getter for property 'typesTimeLine'.
	 *
	 * @return Value for property 'typesTimeLine'.
	 */
	public TimeLine<StreetType, StreetTypeTemporal> getTypesTimeLine() {
		return typesTimeLine;
	}

	public Street addTypeTemporal(StreetTypeTemporal temporal) {
		if (typesTimeLine == null) {
			log.debug("Creating new street types timeline");
			typesTimeLine = new TimeLine<StreetType, StreetTypeTemporal>(temporal);
		} else {
			log.debug("Adding  new street type interval");
			typesTimeLine = DateIntervalUtil.addInterval(typesTimeLine, temporal);
		}

		return this;
	}

	/**
	 * Setter for property 'typesTimeLine'.
	 *
	 * @param typesTimeLine Value to set for property 'typesTimeLine'.
	 */
	public void setTypesTimeLine(TimeLine<StreetType, StreetTypeTemporal> typesTimeLine) {
		this.typesTimeLine = typesTimeLine;
	}

	/**
	 * Setter for property 'typeTemporals'.
	 *
	 * @param typeTemporals Value to set for property 'typeTemporals'.
	 */
	public void setTypeTemporals(SortedSet<StreetTypeTemporal> typeTemporals) {
		typesTimeLine = new TimeLine<StreetType, StreetTypeTemporal>(typeTemporals);
	}

	/**
	 * Getter for property 'typesTimeLine'.
	 *
	 * @return Value for property 'typesTimeLine'.
	 */
	public SortedSet<StreetTypeTemporal> getTypeTemporals() {
		if (typesTimeLine == null) {
			return EMPTY_SORTED_SET;
		}
		return typesTimeLine.getIntervalsSet();
	}

	/**
	 * Find value for date
	 *
	 * @param dt Date to get value for
	 * @return Value which interval includes specified date, or <code>null</code> if not
	 *         found
	 */
	public StreetType getTypeForDate(Date dt) {
		if (typesTimeLine == null) {
			return null;
		}
		List<StreetTypeTemporal> intervals = typesTimeLine.getIntervals();
		for (StreetTypeTemporal di : intervals) {
			if (DateIntervalUtil.includes(dt, di)) {
				return di.getValue();
			}
		}

		return null;
	}

	/**
	 * Find value for current date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not
	 *         found
	 */
	public StreetType getCurrentType() {
		return getTypeForDate(DateIntervalUtil.now());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Street)) {
			return false;
		}

		Street that = (Street) obj;

		return new EqualsBuilder()
				.appendSuper(super.equals(obj))
				.append(typesTimeLine, that.typesTimeLine)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("Name", getCurrentName())
				.append("Type", getCurrentType())
				.toString();
	}
	
	public String format(Locale locale, boolean shortMode) throws FlexPayException {
		StreetTypeTranslation typeTanslation = TranslationUtil.getTranslation(getCurrentType().getTranslations(), locale);
		StreetNameTranslation nameTanslation = TranslationUtil.getTranslation(getCurrentName().getTranslations(), locale);
		String typeStr = typeTanslation == null ? "" : (shortMode ? typeTanslation.getShortName() + "." : typeTanslation.getName());
		String nameStr = nameTanslation == null ? "" : nameTanslation.getName();
		
		return typeStr + " " + nameStr;
	}
}
