package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.ReferencesHistoryGenerator;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.Subdivision;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

public class SubdivisionReferencesHistoryGenerator implements ReferencesHistoryGenerator<Subdivision> {

	private OrganizationHistoryGenerator organizationHistoryGenerator;
	private SubdivisionHistoryGenerator subdivisionHistoryGenerator;

	@Override
	public void generateReferencesHistory(@NotNull Subdivision obj) {
		organizationHistoryGenerator.generateFor(obj.getHeadOrganization());
		Organization juridicalPerson = obj.getJuridicalPerson();
		if (juridicalPerson != null) {
			organizationHistoryGenerator.generateFor(juridicalPerson);
		}

		Subdivision parentSubdivision = obj.getParentSubdivision();
		if (parentSubdivision != null) {
			subdivisionHistoryGenerator.generateFor(parentSubdivision);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
    }

	@Required
	public void setOrganizationHistoryGenerator(OrganizationHistoryGenerator organizationHistoryGenerator) {
		this.organizationHistoryGenerator = organizationHistoryGenerator;
	}

	@Required
	public void setSubdivisionHistoryGenerator(SubdivisionHistoryGenerator subdivisionHistoryGenerator) {
		this.subdivisionHistoryGenerator = subdivisionHistoryGenerator;
	}
}
