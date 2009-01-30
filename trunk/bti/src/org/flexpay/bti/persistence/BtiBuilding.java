package org.flexpay.bti.persistence;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.Collections;
import java.util.Date;

public class BtiBuilding extends Building {

	private Set<BuildingAttributeBase> attributes = Collections.emptySet();

	public BtiBuilding() {
	}

	public BtiBuilding(@NotNull Long id) {
		super(id);
	}

	public BtiBuilding(@NotNull Stub<BtiBuilding> stub) {
		super(stub.getId());
	}

	public Set<BuildingAttributeBase> getAttributes() {
		return attributes;
	}

	@Nullable
    public BuildingAttributeBase getAttribute(BuildingAttributeType attributeType) {
        for (BuildingAttributeBase attribute : attributes) {
            if (attribute.getAttributeType().equals(attributeType)) {
                return attribute;
            }
        }

        return null;
    }

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setAttributes(Set<BuildingAttributeBase> attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(BuildingAttributeBase attribute) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (attributes == Collections.EMPTY_SET) {
			attributes = CollectionUtils.set();
		}

		attribute.setBuilding(this);

		BuildingAttributeBase oldAttribute = getAttribute(attribute.getAttributeType());
		if (oldAttribute != null) {
			attributes.remove(oldAttribute);
		}
		attributes.add(attribute);
	}

	public void setNormalAttribute(BuildingAttributeType type, String value) {

		BuildingAttributeBase attribute = new BuildingAttribute();
		attribute.setAttributeType(type);
		attribute.setCurrentValue(value);
		setAttribute(attribute);
	}

	public void setCurrentTmpAttribute(BuildingAttributeType type, String value) {
		setTmpAttributeForDate(type, value, DateUtil.now());
	}

	public void setTmpAttributeForDate(BuildingAttributeType type, String value, Date begin) {
		setTmpAttributeForDates(type, value, begin, ApplicationConfig.getFutureInfinite());
	}

	public void setTmpAttributeForDates(BuildingAttributeType type, String value, Date begin, Date end) {

		BuildingAttributeBase attribute = new BuildingTempAttribute();
		attribute.setAttributeType(type);
		attribute.setValueForDates(value, begin, end);
		setAttribute(attribute);
	}
}
