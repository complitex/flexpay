package org.flexpay.ab.persistence;

import java.util.Collections;
import java.util.List;

public class IdentityType  implements java.io.Serializable {

    private int id;
    private List<IdentityTypeName> identityTypeNames = Collections.emptyList();
    private List<PersonIdentity> personIdentities = Collections.emptyList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<IdentityTypeName> getIdentityTypeNames() {
        return identityTypeNames;
    }

    public void setIdentityTypeNames(List<IdentityTypeName> identityTypeNames) {
        this.identityTypeNames = identityTypeNames;
    }

    public List<PersonIdentity> getPersonIdentities() {
        return personIdentities;
    }

    public void setPersonIdentities(List<PersonIdentity> personIdentities) {
        this.personIdentities = personIdentities;
    }
}


