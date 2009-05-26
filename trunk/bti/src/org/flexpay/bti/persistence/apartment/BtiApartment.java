package org.flexpay.bti.persistence.apartment;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class BtiApartment extends Apartment {

	private Set<ApartmentAttributeBase> attributes = Collections.emptySet();

	public BtiApartment() {
	}

	public BtiApartment(@NotNull Long id) {
		super(id);
	}

	public BtiApartment(@NotNull Stub<BtiApartment> stub) {
		super(stub.getId());
	}

	public Set<ApartmentAttributeBase> getAttributes() {
		return attributes;
	}

	@Nullable
    public ApartmentAttributeBase getAttribute(ApartmentAttributeType attributeType) {

        for (ApartmentAttributeBase attribute : attributes) {
            if (attribute.getAttributeType().equals(attributeType)) {
                return attribute;
            }
        }

        return null;
    }

	public void setAttributes(Set<ApartmentAttributeBase> attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(ApartmentAttributeBase attribute) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (attributes == Collections.EMPTY_SET) {
			attributes = CollectionUtils.set();
		}

		attribute.setApartment(this);

		ApartmentAttributeBase oldAttribute = getAttribute(attribute.getAttributeType());
		if (oldAttribute != null) {
			attributes.remove(oldAttribute);
		}
		attributes.add(attribute);
	}

	public void setNormalAttribute(ApartmentAttributeType type, String value) {

		ApartmentAttributeBase attribute = new ApartmentAttribute();
		attribute.setAttributeType(type);
		attribute.setCurrentValue(value);
		setAttribute(attribute);
	}

	public void setCurrentTmpAttribute(ApartmentAttributeType type, String value) {
		setTmpAttributeForDate(type, value, DateUtil.now());
	}

	public void setTmpAttributeForDate(ApartmentAttributeType type, String value, Date begin) {
		setTmpAttributeForDates(type, value, begin, ApplicationConfig.getFutureInfinite());
	}

	public void setTmpAttributeForDates(ApartmentAttributeType type, String value, Date begin, Date end) {

		ApartmentAttributeBase attribute = new ApartmentTempAttribute();
		attribute.setAttributeType(type);
		attribute.setValueForDates(value, begin, end);
		setAttribute(attribute);
	}

	public void addAttribute(ApartmentAttributeBase attribute) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (attributes == Collections.EMPTY_SET) {
			attributes = CollectionUtils.set();
		}

		attribute.setApartment(this);
		attributes.add(attribute);
	}

    public void removeAttribute(ApartmentAttributeType type) {
        if (attributes.isEmpty()) {
			return;
		}

        ApartmentAttributeBase theAttribute = null;
        for (ApartmentAttributeBase attribute : attributes) {
            ApartmentAttributeType attributeType = attribute.getAttributeType();

            if (attributeType != null && attributeType.equals(type)) {
                theAttribute = attribute;
            }
        }

        if (theAttribute != null) {
            attributes.remove(theAttribute);
        }
    }

    public void removeAttribute(ApartmentAttributeBase attribute) {
        if (attributes.isEmpty()) {
			return;
		}

        attributes.remove(attribute);
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				toString();
	}

}
