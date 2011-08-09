package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.CollectionUtils.treeSet;
import static org.flexpay.common.util.DateUtil.format;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

public class Town extends NameTimeDependentChild<TownName, TownNameTemporal> {

	private static final SortedSet<TownTypeTemporal> EMPTY_SORTED_SET =
			Collections.unmodifiableSortedSet(new TreeSet<TownTypeTemporal>());

	private SortedSet<TownTypeTemporal> typeTemporals = EMPTY_SORTED_SET;
	private TimeLine<TownType, TownTypeTemporal> typesTimeLine;

	private Set<District> districts = set();
	private Set<Street> streets = set();

	public Town() {
	}

	public Town(Long id) {
		super(id);
	}

	public Town(@NotNull Stub<Town> stub) {
		super(stub.getId());
	}

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <town>\n");

        if (ACTION_UPDATE.equals(action)) {
            builder.append("        <id>").append(id).append("</id>\n");
        }

        if (ACTION_INSERT.equals(action) || ACTION_UPDATE.equals(action)) {
            builder.append("        <parentId>").append(getRegion().getId()).append("</parentId>\n").
                    append("        <nameDate>").append(format(nameDate)).append("</nameDate>\n").
                    append("        <typeId>").append(getTypeForDate(nameDate).getId()).append("</typeId>\n").
                    append("        <translations>\n");
            for (TownNameTranslation translation : getNameForDate(nameDate).getTranslations()) {
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
        }

        builder.append("    </town>\n");

        return builder.toString();
    }

	/**
	 * Create a new empty temporal
	 *
	 * @return empty temporal
	 */
    @Override
	protected TownNameTemporal getEmptyTemporal() {
		return new TownNameTemporal(this);
	}

	public Set<District> getDistricts() {
		return districts;
	}

	public void setDistricts(Set<District> districts) {
		this.districts = districts;
	}

	public Set<Street> getStreets() {
		return streets;
	}

	public void setStreets(Set<Street> streets) {
		this.streets = streets;
	}

	public TimeLine<TownType, TownTypeTemporal> getTypesTimeLine() {

		if (typesTimeLine == null) {
			TownTypeTemporal temporal = new TownTypeTemporal();
			temporal.setTown(this);
			return new TimeLine<TownType, TownTypeTemporal>(temporal);
		}

		return typesTimeLine;
	}

	public void setTypesTimeLine(TimeLine<TownType, TownTypeTemporal> typesTimeLine) {
		this.typesTimeLine = typesTimeLine;

		if (typeTemporals == EMPTY_SORTED_SET) {
			typeTemporals = treeSet();
		}
		typeTemporals.addAll(typesTimeLine.getIntervals());
	}

	public void setTypeTemporals(SortedSet<TownTypeTemporal> temporals) {
		this.typeTemporals = temporals;
		typesTimeLine = new TimeLine<TownType, TownTypeTemporal>(temporals);
	}

	public SortedSet<TownTypeTemporal> getTypeTemporals() {

		for (TownTypeTemporal temporal : typeTemporals) {
			if (temporal.getValue() != null && temporal.getValue().isNew()) {
				temporal.setValue(null);
			}
		}

		return typeTemporals;
	}

    public void addTypeTemporal(TownTypeTemporal temporal) {
        if (typesTimeLine == null) {
            typesTimeLine = new TimeLine<TownType, TownTypeTemporal>(temporal);
        } else {
            typesTimeLine = DateIntervalUtil.addInterval(typesTimeLine, temporal);
        }

        if (typeTemporals == EMPTY_SORTED_SET) {
            typeTemporals = treeSet();
        }
        typeTemporals.addAll(typesTimeLine.getIntervals());
    }

    public void setType(TownType type) {
        setTypeForDate(type, DateUtil.now());
    }

    public void setTypeForDate(TownType type, Date beginDate) {
        setTypeForDates(type, beginDate, getFutureInfinite());
    }

    public void setTypeForDates(TownType type, Date beginDate, Date endDate) {
        TownTypeTemporal temporal = new TownTypeTemporal();
        temporal.setBegin(beginDate);
        temporal.setEnd(endDate);
        temporal.setValue(type);
        temporal.setTown(this);

        addTypeTemporal(temporal);
    }

	/**
	 * Find temporal for date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public TownTypeTemporal getCurrentTypeTemporal() {
		return getTypeTemporalForDate(DateUtil.now());
	}

	/**
	 * Find temporal for date
	 *
	 * @param dt Date to get value for
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public TownTypeTemporal getTypeTemporalForDate(Date dt) {
		if (typesTimeLine == null) {
			return null;
		}
		List<TownTypeTemporal> intervals = typesTimeLine.getIntervals();
		for (TownTypeTemporal di : intervals) {
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
	public TownType getTypeForDate(Date dt) {
		TownTypeTemporal di = getTypeTemporalForDate(dt);
		return di != null ? di.getValue() : null;
	}

	/**
	 * Find value for current date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not found
	 */
	@Nullable
	public TownType getCurrentType() {
		TownTypeTemporal di = getCurrentTypeTemporal();
		return di != null ? di.getValue() : null;
	}

	public void setRegion(Region region) {
		setParent(region);
	}

	public Region getRegion() {
		return (Region) getParent();
	}

    @NotNull
    public Stub<Region> getRegionStub() {
        return new Stub<Region>(getParentStub().getId());
    }

	public Country getCountry() {
		return getRegion().getCountry();
	}

	public void setName(TownName name) {
		setNameForDate(name, DateUtil.now());
	}

	public void setNameForDate(TownName name, Date beginDate) {
		setNameForDates(name, beginDate, getFutureInfinite());
	}

	public void setNameForDates(TownName name, Date beginDate, Date endDate) {
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

		TownNameTemporal temporal = new TownNameTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(name);
		temporal.setObject(this);

		addNameTemporal(temporal);
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Town)) {
            return false;
        }

        Town that = (Town) obj;

        return new EqualsBuilder()
                .append(typesTimeLine, that.typesTimeLine)
                .appendSuper(super.equals(obj))
                .isEquals();
    }

}
