package org.flexpay.eirc.dao;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class QuittanceDaoHelper {

	public static QuittanceDaoExt.CreateQuittancesOptions createOptions(
			@NotNull Stub<EircServiceOrganization> organizationStub, @NotNull Stub<Town> townStub,
			Date dateFrom, Date dateTill, boolean deleteEmptyQuittances) {

		QuittanceDaoExt.CreateQuittancesOptions options = new QuittanceDaoExt.CreateQuittancesOptions();
		options.organizationStub = organizationStub;
		options.townStub = townStub;
		options.dateFrom = dateFrom;
		options.dateTill = dateTill;
		options.deleteEmptyQuittances = deleteEmptyQuittances;

		return options;
	}
}
