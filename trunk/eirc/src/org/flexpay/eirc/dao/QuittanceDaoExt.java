package org.flexpay.eirc.dao;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircServiceOrganization;

import java.util.Date;

/**
 * Interface provides functionality to prepare quittances
 */
public interface QuittanceDaoExt {

	/**
	 * Generate current snapshot of details and create quittances for the following processing
	 *
	 * @param options Operation options
	 * @return number of generated quittances
	 */
	long createQuittances(CreateQuittancesOptions options);

	/**
	 * Simple create quittances operation properties container
	 */
	public static class CreateQuittancesOptions {

		public Stub<EircServiceOrganization> organizationStub;
		public Stub<Town> townStub;
		public Date dateFrom;
		public Date dateTill;
		public boolean deleteEmptyQuittances;

		@Override
		public String toString() {
			return new ToStringBuilder(this).
					append("organizationStub", organizationStub).
					append("townStub", townStub).
					append("dateFrom", dateFrom).
					append("dateTill", dateTill).
					append("deleteEmptyQuittances", deleteEmptyQuittances).
					toString();
		}
	}
}
