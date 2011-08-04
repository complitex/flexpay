package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Collection;
import java.util.Date;

import static org.flexpay.common.util.CollectionUtils.set;

public abstract class EsbXmlSyncObject extends DomainObjectWithStatus {

    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_INSERT = "insert";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_UPDATE_STREET_DISTRICTS = "update_sd";
    public static final String ACTION_UPDATE_ADDRESS_SET_PRIMARY = "set_primary";

    protected String action;
    protected Collection<Long> ids = set();
    protected Collection<Long> districtIds = set();
    protected Date nameDate;

    public EsbXmlSyncObject() {
    }

    protected EsbXmlSyncObject(Long id) {
        super(id);
    }

    public abstract String getXmlString();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Collection<Long> getIds() {
        return ids;
    }

    public void setIds(Collection<Long> ids) {
        this.ids = ids;
    }

    public Collection<Long> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(Collection<Long> districtIds) {
        this.districtIds = districtIds;
    }

    public Date getNameDate() {
        return nameDate;
    }

    public void setNameDate(Date nameDate) {
        this.nameDate = nameDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("status", status).
                append("action", action).
                append("ids", ids).
                append("districtIds", districtIds).
                append("nameDate", nameDate).
                toString();
    }
}
