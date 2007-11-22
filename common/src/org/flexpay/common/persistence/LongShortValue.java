package org.flexpay.common.persistence;

public class LongShortValue {
    private String shortValue;
    private String longValue;

    public String getShortValue() {
        return shortValue;
    }

    public void setShortValue(String shortValue) {
        this.shortValue = shortValue;
    }

    public String getLongValue() {
        return longValue;
    }

    public void setLongValue(String longValue) {
        this.longValue = longValue;
    }

    public LongShortValue( String longValue, String shortValue) {
        this.shortValue = shortValue;
        this.longValue = longValue;
    }
}
