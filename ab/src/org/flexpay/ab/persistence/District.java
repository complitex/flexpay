package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.flexpay.common.util.DateUtil.format;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

public class District extends NameTimeDependentChild<DistrictName, DistrictNameTemporal> {

	private Set<StreetDistrictRelation> streetDistricts = Collections.emptySet();

	public District() {
	}

	public District(Long id) {
		super(id);
	}

    public District(Stub<District> district) {
		super(district.getId());
	}

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <district>\n");

        if (ACTION_INSERT.equals(action) || ACTION_UPDATE.equals(action)) {
            builder.append("        <id>").append(id).append("</id>\n").
                    append("        <parentId>").append(getTown().getId()).append("</parentId>\n").
                    append("        <nameDate>").append(format(nameDate)).append("</nameDate>\n").
                    append("        <translations>\n");
            for (DistrictNameTranslation translation : getNameForDate(nameDate).getTranslations()) {
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

        builder.append("    </district>\n");

        return builder.toString();
    }

    @Override
	protected DistrictNameTemporal getEmptyTemporal() {
		return new DistrictNameTemporal(this);
	}

	public Set<StreetDistrictRelation> getStreetDistricts() {
		return streetDistricts;
	}

	public void setStreetDistricts(Set<StreetDistrictRelation> streetDistricts) {
		this.streetDistricts = streetDistricts;
	}

	public void setNameForDate(DistrictName name, Date beginDate) {
		setNameForDates(name, beginDate, getFutureInfinite());
	}

	public void setNameForDates(DistrictName name, Date beginDate, Date endDate) {

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

		DistrictNameTemporal temporal = new DistrictNameTemporal();
		temporal.setBegin(beginDate);
		temporal.setEnd(endDate);
		temporal.setValue(name);
		temporal.setObject(this);

		addNameTemporal(temporal);
	}

	@NotNull
	public Town getTown() {
		return (Town) getParent();
	}

	@NotNull
	public Stub<Town> getTownStub() {
		return new Stub<Town>(getTown());
	}

	@NotNull
	public Region getRegion() {
		return getTown().getRegion();
	}

	@NotNull
	public Country getCountry() {
		return getTown().getCountry();
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof District && super.equals(obj);
	}

}
