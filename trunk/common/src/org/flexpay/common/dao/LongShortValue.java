package org.flexpay.common.dao;
/*
  ���� ������������ ��� ������������� �������� � ��������� �������� ������� �������.
 */
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

    public LongShortValue(String shortValue, String longValue) {
        this.shortValue = shortValue;
        this.longValue = longValue;
    }
}
