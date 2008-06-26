package org.flexpay.eirc.service.imp;

import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.dao.QuittanceDetailsDao;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class QuittanceServiceImpl implements QuittanceService {

	private QuittanceDetailsDao quittanceDetailsDao;

	/**
	 * Create or update a QuittanceDetails record
	 *
	 * @param details QuittanceDetails to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation failure occurs
	 */
	@Transactional(readOnly = false)
	public void save(QuittanceDetails details) throws FlexPayExceptionContainer {
		if (details.isNew()) {
			details.setId(null);
			quittanceDetailsDao.create(details);
		} else {
			quittanceDetailsDao.update(details);
		}
	}

	public void setQuittanceDetailsDao(QuittanceDetailsDao quittanceDetailsDao) {
		this.quittanceDetailsDao = quittanceDetailsDao;
	}
}
