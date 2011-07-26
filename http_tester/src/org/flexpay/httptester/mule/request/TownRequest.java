package org.flexpay.httptester.mule.request;

import java.util.Properties;

public class TownRequest extends MuleRequest {

    static {
        paramNames.add("town.action");
        paramNames.add("town.id");
        paramNames.add("town.id1");
        paramNames.add("town.id2");
        paramNames.add("town.parentId");
        paramNames.add("town.typeId");
        paramNames.add("town.nameDate");
        paramNames.add("town.translation1.name");
        paramNames.add("town.translation1.languageId");
        paramNames.add("town.translation2.name");
        paramNames.add("town.translation2.languageId");
    }

    public TownRequest(Properties props) {
        super(props);
    }

    @Override
    protected String getTemlateFilenamePropertyName() {
        return "mule.request_town.file";
    }
}
