package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class DistrictRequest extends MuleRequest {

    static {
        paramNames.add("district.action");
        paramNames.add("district.id");
        paramNames.add("district.id1");
        paramNames.add("district.id2");
        paramNames.add("district.parentId");
        paramNames.add("district.nameDate");
        paramNames.add("district.translation1.name");
        paramNames.add("district.translation1.languageId");
        paramNames.add("district.translation2.name");
        paramNames.add("district.translation2.languageId");
    }

    public DistrictRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_district.file";
    }
}
