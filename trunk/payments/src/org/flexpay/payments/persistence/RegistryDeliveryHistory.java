package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;

import java.util.Date;

public class RegistryDeliveryHistory extends DomainObject {
    private Registry registry;
    private Date deliveryDate;
    private String email;
    private FPFile spFile;

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FPFile getSpFile() {
        return spFile;
    }

    public void setSpFile(FPFile spFile) {
        this.spFile = spFile;
    }
}
