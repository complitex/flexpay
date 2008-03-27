package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.service.OrganisationService;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.dao.OrganisationDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class OrganisationServiceImpl implements OrganisationService {

	private OrganisationDao organisationDao;

	/**
	 * Find organisation by its id
	 *
	 * @param organisationId Organisation id
	 * @return Organisation if found, or <code>null</code> otherwise
	 */
	public Organisation getOrganisation(String organisationId) {
		List<Organisation> organisations =  organisationDao.findOrganisationsById(
				organisationId);
		return organisations.isEmpty() ? null : organisations.get(0);
	}

	/**
	 * Setter for property 'organisationDao'.
	 *
	 * @param organisationDao Value to set for property 'organisationDao'.
	 */
	public void setOrganisationDao(OrganisationDao organisationDao) {
		this.organisationDao = organisationDao;
	}
}
