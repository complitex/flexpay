package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.CollectionUtils.treeSet;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

public class Street extends NameTimeDependentChild<StreetName, StreetNameTemporal> {

	private static final SortedSet<StreetTypeTemporal> EMPTY_SORTED_SET =
			Collections.unmodifiableSortedSet(new TreeSet<StreetTypeTemporal>());

	private SortedSet<StreetTypeTemporal> typeTemporals = EMPTY_SORTED_SET;
	private TimeLine<StreetType, StreetTypeTemporal> typesTimeLine;

    private Set<StreetDistrictRelation> streetDistricts = set();
	private Set<BuildingAddress> buildingses = set();

	public Street() {
	}

	public Street(Long id) {
		super(id);
	}

	public Street(@NotNull Stub<Street> stub) {
		super(stub.getId());
	}

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <street>\n");

        if (ACTION_UPDATE.equals(action) || ACTION_UPDATE_STREET_DISTRICTS.equals(action)) {
            builder.append("        <id>").append(id).append("</id>\n");
        }

        if (ACTION_INSERT.equals(action) || ACTION_UPDATE.equals(action)) {
            builder.append("        <parentId>").append(getTown().getId()).append("</parentId>\n").
                    append("        <nameDate>").append(DateUtil.format(nameDate)).append("</nameDate>\n").
                    append("        <typeId>").append(getTypeForDate(nameDate).getId()).append("</typeId>\n").
                    append("        <translations>\n");
            for (StreetNameTranslation translation : getNameForDate(nameDate).getTranslations()) {
                builder.append("            <org.flexpay.mule.request.MuleTranslation>\n").
                        append("                <name>").append(translation.getName()).append("</name>\n").
                        append("                <languageId>").append(translation.getLang().getId()).append("</languageId>\n").
                        append("            </org.flexpay.mule.request.MuleTranslation>\n");
            }
            builder.append("        </translations>\n");
        } else if (ACTION_DELETE.equals(action)) {
            builder.append("        <ids>\n");
            for (Long id : ids) {
                builder.append("            <long>").append(id).append("</long>\n");
            }
            builder.append("        </ids>\n");
        } else if (ACTION_UPDATE_STREET_DISTRICTS.equals(action)) {
            builder.append("        <districts>\n");
            for (Long districtsId : districtIds) {
                builder.append("            <long>").append(districtsId).append("</long>\n");
            }
            builder.append("        </districts>\n");
        }

        builder.append("    </street>\n");

        return builder.toString();
    }

	/**
	 * Create a new empty temporal
	 *
	 * @return empty temporal
	 */
    @Override
	protected StreetNameTemporal getEmptyTemporal() {
		return new StreetNameTemporal(this);
	}

	public Set<StreetDistrictRelation> getStreetDistricts() {
		return streetDistricts;
	}

	public void setStreetDistricts(Set<StreetDistrictRelation> streetDistricts) {
		this.streetDistricts = streetDistricts;
	}

	public Set<BuildingAddress> getBuildingses() {
		return buildingses;
	}

	public void setBuildingses(Set<BuildingAddress> buildingses) {
		this.buildingses = buildingses;
	}

	public TimeLine<StreetType, StreetTypeTemporal> getTypesTimeLine() {

		if (typesTimeLine == null) {
			StreetTypeTemporal temporal = new StreetTypeTemporal();
			temporal.setStreet(this);
			return new TimeLine<StreetType, StreetTypeTemporal>(temporal);
		}

		return typesTimeLine;
	}

    public void setTypesTimeLine(TimeLine<StreetType, StreetTypeTemporal> typesTimeLine) {
        this.typesTimeLine = typesTimeLine;

        if (typeTemporals == EMPTY_SORTED_SET) {
            typeTemporals = treeSet();
        }
        typeTemporals.addAll(typesTimeLine.getIntervals());
    }

    public void setTypeTemporals(SortedSet<StreetTypeTemporal> typeTemporals) {
        this.typeTemporals = typeTemporals;
        typesTimeLine = new TimeLine<StreetType, StreetTypeTemporal>(typeTemporals);
    }

    public SortedSet<StreetTypeTemporal> getTypeTemporals() {

        for (StreetTypeTemporal temporal : typeTemporals) {
            if (temporal.getValue() != null && temporal.getValue().isNew()) {
                temporal.setValue(null);
            }
        }

        return typeTemporals;
    }

	public void addTypeTemporal(StreetTypeTemporal temporal) {
        if (typesTimeLine == null) {
            typesTimeLine = new TimeLine<StreetType, StreetTypeTemporal>(temporal);
        } else {
            typesTimeLine = DateIntervalUtil.addInterval(typesTimeLine, temporal);
        }

        if (typeTemporals == EMPTY_SORTED_SET) {
            typeTemporals = treeSet();
        }
        typeTemporals.addAll(typesTimeLine.getIntervals());
	}

	public void setType(StreetType type) {
		setTypeForDate(type, DateUtil.now());
	}

	public void setTypeForDate(StreetType type, Date beginDate) {
		setTypeForDates(type, beginDate, getFutureInfinite());
	}

	public void setTypeForDates(StreetType type, Date beginDate, Date endDate) {
		StreetTypeTemporal temporal = new StreetTypeTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(type);
		temporal.setStreet(this);

		addTypeTemporal(temporal);
	}

    /**
     * Find temporal for date
     *
     * @return Value which interval includes specified date, or <code>null</code> if not found
     */
    @Nullable
    public StreetTypeTemporal getCurrentTypeTemporal() {
        return getTypeTemporalForDate(DateUtil.now());
    }

    /**
     * Find temporal for date
     *
     * @param dt Date to get value for
     * @return Value which interval includes specified date, or <code>null</code> if not found
     */
    @Nullable
    public StreetTypeTemporal getTypeTemporalForDate(Date dt) {
        if (typesTimeLine == null) {
            return null;
        }
        List<StreetTypeTemporal> intervals = typesTimeLine.getIntervals();
        for (StreetTypeTemporal di : intervals) {
            if (DateIntervalUtil.includes(dt, di)) {
                return di;
            }
        }

        return null;
    }

    /**
     * Find value for date
     *
     * @param dt Date to get value for
     * @return Value which interval includes specified date, or <code>null</code> if not found
     */
    @Nullable
    public StreetType getTypeForDate(Date dt) {
        StreetTypeTemporal di = getTypeTemporalForDate(dt);
        return di != null ? di.getValue() : null;
    }

	/**
	 * Find value for current date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public StreetType getCurrentType() {
        StreetTypeTemporal di = getCurrentTypeTemporal();
        return di != null ? di.getValue() : null;
	}

    public void setTown(Town town) {
        setParent(town);
    }

	public Town getTown() {
		return (Town) getParent();
	}

    @NotNull
	public Stub<Town> getTownStub() {
		return new Stub<Town>(getTown());
	}

	public Region getRegion() {
		return getTown().getRegion();
	}

	public Country getCountry() {
		return getTown().getCountry();
	}

	public void setName(StreetName name) {
		setNameForDate(name, DateUtil.now());
	}

	public void setNameForDate(StreetName name, Date beginDate) {
		setNameForDates(name, beginDate, getFutureInfinite());
	}

	public void setNameForDates(StreetName name, Date beginDate, Date endDate) {
		if (beginDate.after(endDate)) {
			throw new RuntimeException("Invalid begin-end dates: [" + DateUtil.format(beginDate) +
									   ", " + DateUtil.format(endDate) + "]");
		}
		if (beginDate.before(getPastInfinite())) {
			beginDate = getPastInfinite();
		}
		if (endDate.after(getFutureInfinite())) {
			endDate = getFutureInfinite();
		}

		name.setObject(this);

		StreetNameTemporal temporal = new StreetNameTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(name);
		temporal.setObject(this);

		addNameTemporal(temporal);
	}

    public String format(Locale locale, boolean shortMode) throws FlexPayException {
        StringBuilder formatted = new StringBuilder();

        StreetTypeTranslation typeTanslation = getTypeTranslation(locale);
        if (typeTanslation != null) {
            if (shortMode) {
                formatted.append(typeTanslation.getShortName()).append(".");
            } else {
                formatted.append(typeTanslation.getName());
            }
        }

        StreetNameTranslation nameTranslation = getNameTranslation(locale);
        if (nameTranslation != null) {
            formatted.append(" ").append(nameTranslation.getName());
        }

        return formatted.toString();
    }

    @Nullable
    private StreetNameTranslation getNameTranslation(Locale locale) {
        StreetName name = getCurrentName();
        StreetNameTranslation nameTranslation = null;
        if (name != null) {
            nameTranslation = TranslationUtil.getTranslation(name.getTranslations(), locale);
        }
        return nameTranslation;
    }

    @Nullable
    private StreetTypeTranslation getTypeTranslation(Locale locale) {
        StreetType type = getCurrentType();
        StreetTypeTranslation typeTanslation = null;
        if (type != null) {
            typeTanslation = TranslationUtil.getTranslation(type.getTranslations(), locale);
        }
        return typeTanslation;
    }

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
                .append(typesTimeLine, that.typesTimeLine)
                .appendSuper(super.equals(obj))
                .isEquals();
    }

}
