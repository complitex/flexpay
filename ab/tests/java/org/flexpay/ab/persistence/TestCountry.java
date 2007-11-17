package org.flexpay.ab.persistence;

import junit.framework.TestCase;

public class TestCountry extends TestCase {

    public void testCountryName(){
        Country country = new Country();
        String lang = "english";
        String longName = "Russia";
        String shortName = "RUS";

        country.setCountryName(longName,shortName,lang);
        assertEquals(country.getCountryName(lang).getLongValue(),longName);
        assertEquals(country.getCountryName(lang).getShortValue(),shortName);

        country.setCountryName("","",lang);
        assertEquals(country.getCountryName(lang).getLongValue(),"");
        assertEquals(country.getCountryName(lang).getShortValue(),"");
    }
}
