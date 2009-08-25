package org.flexpay.eirc.persistence.exchange;

import org.flexpay.orgs.persistence.Organization;

public interface ExternalOrganizationAccountAwareUpdate {

	void setExternalAccount(String accountNumber, Organization org);
}
