package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class CountryRequest extends MuleRequest {

    static {
        paramNames.add("country.action");
        paramNames.add("country.translation1.name");
        paramNames.add("country.translation1.shortName");
        paramNames.add("country.translation1.languageId");
        paramNames.add("country.translation2.name");
        paramNames.add("country.translation2.shortName");
        paramNames.add("country.translation2.languageId");
    }

    public CountryRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_country.file";
    }
}
