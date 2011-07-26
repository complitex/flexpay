package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class RegionRequest extends MuleRequest {

    static {
        paramNames.add("region.action");
        paramNames.add("region.id");
        paramNames.add("region.id1");
        paramNames.add("region.id2");
        paramNames.add("region.parentId");
        paramNames.add("region.nameDate");
        paramNames.add("region.translation1.name");
        paramNames.add("region.translation1.languageId");
        paramNames.add("region.translation2.name");
        paramNames.add("region.translation2.languageId");
    }

    public RegionRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_region.file";
    }
}
