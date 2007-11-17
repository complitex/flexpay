package org.flexpay.common.persistence;

import java.io.Serializable;

public class AttributeValueKey implements Serializable {
    private String name;
    private String lang;

    public AttributeValueKey() {
    }

    public AttributeValueKey(String name, String lang) {
        this.name = name;
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public String getLang() {
        return lang;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        final AttributeValueKey that = (AttributeValueKey) o;

        if (lang != null ? !lang.equals(that.getLang()) : that.getLang() != null) {
            return false;
        }
        return name.equals(that.getName());

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String toString() {
        return name + ";" + lang;
    }
}


