package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.DateUtil.format;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

/**
 * Region
 */
public class Region extends NameTimeDependentChild<RegionName, RegionNameTemporal> {

	private Set<Town> towns = set();

	public Region() {
	}

	public Region(Long id) {
		super(id);
	}

	public Region(@NotNull Stub<Region> stub) {
		super(stub.getId());
	}

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <region>\n");

        if (ACTION_UPDATE.equals(action)) {
            builder.append("        <id>").append(id).append("</id>\n");
        }

        if (ACTION_INSERT.equals(action) || ACTION_UPDATE.equals(action)) {
            builder.append("        <parentId>").append(getCountry().getId()).append("</parentId>\n").
                    append("        <nameDate>").append(format(nameDate)).append("</nameDate>\n").
                    append("        <translations>\n");
            for (RegionNameTranslation translation : getNameForDate(nameDate).getTranslations()) {
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

        builder.append("    </region>\n");

        return builder.toString();
    }

    /**
	 * Create a new empty temporal
	 *
	 * @return empty temporal
	 */
    @Override
	protected RegionNameTemporal getEmptyTemporal() {
		return new RegionNameTemporal(this);
	}

	public Set<Town> getTowns() {
		return this.towns;
	}

	public void setTowns(Set<Town> towns) {
		this.towns = towns;
	}

	public Stub<Country> getCountryStub() {
		return new Stub<Country>(getCountry());
	}

	public void setCountry(Country country) {
		setParent(country);
	}

	public Country getCountry() {
		return (Country) getParent();
	}

	public void setNameForDate(RegionName name, Date beginDate) {
		setNameForDates(name, beginDate, getFutureInfinite());
	}

	public void setNameForDates(RegionName name, Date beginDate, Date endDate) {
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

		RegionNameTemporal temporal = new RegionNameTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(name);
		temporal.setObject(this);

		addNameTemporal(temporal);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Region && super.equals(obj);
	}
}
