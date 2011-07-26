package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class StreetRequest extends MuleRequest {

    static {
        paramNames.add("street.action");
        paramNames.add("street.id");
        paramNames.add("street.id1");
        paramNames.add("street.id2");
        paramNames.add("street.parentId");
        paramNames.add("street.typeId");
        paramNames.add("street.districtId1");
        paramNames.add("street.districtId2");
        paramNames.add("street.nameDate");
        paramNames.add("street.translation1.name");
        paramNames.add("street.translation1.languageId");
        paramNames.add("street.translation2.name");
        paramNames.add("street.translation2.languageId");
    }

    public StreetRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_street.file";
    }
}
