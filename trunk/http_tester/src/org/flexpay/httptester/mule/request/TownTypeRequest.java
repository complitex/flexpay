package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class TownTypeRequest extends MuleRequest {

    static {
        paramNames.add("townType.action");
        paramNames.add("townType.id");
        paramNames.add("townType.id1");
        paramNames.add("townType.id2");
        paramNames.add("townType.translation1.name");
        paramNames.add("townType.translation1.shortName");
        paramNames.add("townType.translation1.languageId");
        paramNames.add("townType.translation2.name");
        paramNames.add("townType.translation2.shortName");
        paramNames.add("townType.translation2.languageId");
    }

    public TownTypeRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_townType.file";
    }
}
