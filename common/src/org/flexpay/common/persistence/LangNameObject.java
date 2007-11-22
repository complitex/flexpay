package org.flexpay.common.persistence;

import java.util.Map;
import java.util.HashMap;


public class LangNameObject {
    private Map<String, LongShortValue> names = new HashMap<String, LongShortValue>();

    /**
     * Setter for country names
     * @param names
     */
    public void setNames(Map<String, LongShortValue> names) {
        this.names = names;
    }

    public LongShortValue getName(String lang){
        LongShortValue lsv = this.names.get(lang);
        if (lsv == null) lsv = new LongShortValue("","");
        return lsv;
    }

    public void setName(String longName, String shortName, String lang){
        if (lang == null || lang.length() == 0) {
            throw new NullPointerException("setName: invalid lang : name=" + longName + " lang=" + lang);
        }
        if (longName !=null && longName.length()>0){
            setName(new LongShortValue(longName,shortName),lang);
        } else {
            this.names.remove(lang);
        }
    }

    public void setName(LongShortValue countryName, String lang){
        this.names.put(lang,countryName);
    }

}
