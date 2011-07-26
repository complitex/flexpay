package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class StreetTypeRequest extends MuleRequest {

    static {
        paramNames.add("streetType.action");
        paramNames.add("streetType.id");
        paramNames.add("streetType.id1");
        paramNames.add("streetType.id2");
        paramNames.add("streetType.translation1.name");
        paramNames.add("streetType.translation1.shortName");
        paramNames.add("streetType.translation1.languageId");
        paramNames.add("streetType.translation2.name");
        paramNames.add("streetType.translation2.shortName");
        paramNames.add("streetType.translation2.languageId");
    }

    public StreetTypeRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_streetType.file";
    }
}
