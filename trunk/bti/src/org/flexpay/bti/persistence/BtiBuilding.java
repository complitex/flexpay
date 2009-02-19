package org.flexpay.bti.persistence;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
        if (attributes.isEmpty()) {
            return null;
        }

        for (BuildingAttributeBase attribute : attributes) {
            if (attribute.getAttributeType().equals(attributeType)) {
                return attribute;
            }
        }

        return null;
    }

	public void setAttributes(Set<BuildingAttributeBase> attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(BuildingAttributeBase attribute) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (attributes.isEmpty()) {
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

	public void addAttribute(BuildingAttributeBase attribute) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (attributes.isEmpty()) {
			attributes = CollectionUtils.set();
		}

		attribute.setBuilding(this);
		attributes.add(attribute);
	}

    public void removeAttribute(BuildingAttributeType type) {
        if (attributes.isEmpty()) {
			return;
		}

        BuildingAttributeBase theAttribute = null;
        for (BuildingAttributeBase attribute : attributes) {
            BuildingAttributeType attributeType = attribute.getAttributeType();

            if (attributeType != null && attributeType.equals(type)) {
                theAttribute = attribute;
            }
        }

        if (theAttribute != null) {
            attributes.remove(theAttribute);
        }
    }

    public void removeAttribute(BuildingAttributeBase attribute) {
        if (attributes.isEmpty()) {
			return;
		}

        attributes.remove(attribute);
    }

}
