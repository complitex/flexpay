package org.flexpay.eirc.service.imp;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.QuittanceDao;
import org.flexpay.eirc.dao.QuittanceDaoExt;
import org.flexpay.eirc.dao.QuittanceDetailsDao;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class QuittanceServiceImpl implements QuittanceService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private QuittanceDetailsDao quittanceDetailsDao;
	private QuittanceDao quittanceDao;

	private QuittanceDaoExt quittanceDaoExt;

	private QuittanceNumberService quittanceNumberService;

	/**
	 * Create or update a QuittanceDetails record
	 *
	 * @param details QuittanceDetails to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation failure occurs
	 */
	@Transactional (readOnly = false)
	public void save(QuittanceDetails details) throws FlexPayExceptionContainer {
		if (details.isNew()) {
			details.setId(null);
			quittanceDetailsDao.create(details);
		} else {
			quittanceDetailsDao.update(details);
		}
	}

	@Transactional (readOnly = false)
	public void generateForServiceOrganization(Stub<ServiceOrganization> stub,
											   Date dateFrom, Date dateTill) {

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting quittances generation at {}", new Date());
		}

		quittanceDaoExt.createQuittances(dateFrom, dateTill);

		if (log.isInfoEnabled()) {
			log.info("Quittances generation finished, time took: {} ms", System.currentTimeMillis() - time);
		}
	}

	@NotNull
	public List<Quittance> getQuittances(Stub<ServiceOrganization> stub, Date dateFrom, Date dateTill) {
		return quittanceDao.listQuittancesForPrinting(stub.getId(), dateFrom, dateTill);
	}

	/**
	 * Read full quittance details
	 *
	 * @param stub Quittance stub
	 * @return Quittance if found, or <code>null</code> otherwise
	 */
	public Quittance readFull(@NotNull Stub<Quittance> stub) {

		return quittanceDao.readFull(stub.getId());
	}

	/**
	 * Find quittance by generated number
	 *
	 * @param quittanceNumber Generated quittance number
	 * @return Quittance if found, or <code>null</code> otherwise
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if <code>quittanceNumber</code> has invalid format
	 */
	@Nullable
	public Quittance findByNumber(@NotNull String quittanceNumber) throws FlexPayException {

		QuittanceNumberService.QuittanceNumberInfo info = quittanceNumberService.parseInfo(quittanceNumber);
		List<Quittance> quittances = quittanceDao.findQuittanceByNumber(
				info.getAccountNumber(), info.getMonth(), info.getNumber());
		if (quittances.size() == 0) {
			return null;
		}
		if (quittances.size() > 1) {
			throw new FlexPayException("Duplicate quittances?", "eirc.error.quittance.duplicates", quittanceNumber);
		}

		return quittances.get(0);
	}

	@Required
	public void setQuittanceDao(QuittanceDao quittanceDao) {
		this.quittanceDao = quittanceDao;
	}

	@Required
	public void setQuittanceDetailsDao(QuittanceDetailsDao quittanceDetailsDao) {
		this.quittanceDetailsDao = quittanceDetailsDao;
	}

	@Required
	public void setQuittanceDaoExt(QuittanceDaoExt quittanceDaoExt) {
		this.quittanceDaoExt = quittanceDaoExt;
	}

	@Required
	public void setQuittanceNumberService(QuittanceNumberService quittanceNumberService) {
		this.quittanceNumberService = quittanceNumberService;
	}
}
