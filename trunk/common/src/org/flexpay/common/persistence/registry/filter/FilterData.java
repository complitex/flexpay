package org.flexpay.common.persistence.registry.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import static org.flexpay.common.persistence.registry.filter.StringFilter.*;

public class FilterData {

    private String string;
    private Long type;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public boolean hasIncorrectType() {
        return !TYPE_TOWN.equals(type) && !TYPE_STREET.equals(type) && !TYPE_BUILDING.equals(type) && !TYPE_APARTMENT.equals(type);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("string", string).
                append("type", type).
                toString();
    }
}
