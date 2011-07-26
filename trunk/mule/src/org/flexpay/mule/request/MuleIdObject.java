package org.flexpay.mule.request;

import java.io.Serializable;
import java.util.Set;

public class MuleIdObject implements Serializable {

    protected Set<Long> ids;

    protected Long id;

    public Set<Long> getIds() {
        return ids;
    }

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
