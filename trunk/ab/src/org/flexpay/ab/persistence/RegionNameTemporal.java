package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.util.DateUtil;

public class RegionNameTemporal extends NameDateInterval<RegionName, RegionNameTemporal> {

	/**
	 * Constructs a new RegionNameTemporal.
	 */
	public RegionNameTemporal() {
		super(new RegionName());
	}



	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param di Another name temporal
	 */
	private RegionNameTemporal(NameDateInterval<RegionName, RegionNameTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	public RegionNameTemporal(Region region) {
		this();
		setObject(region);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof RegionNameTemporal)) {
			return false;
		}
		return super.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

    @Override
	protected RegionNameTemporal doGetCopy(NameDateInterval<RegionName, RegionNameTemporal> di) {
		return new RegionNameTemporal(di);
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("id", getId()).
                append("begin", DateUtil.format(getBegin())).
                append("end", DateUtil.format(getEnd())).
                append("invalidDate", DateUtil.format(getInvalidDate())).
                append("createDate", DateUtil.format(getCreateDate())).
                append("value", getValue()).
                toString();
    }

}
