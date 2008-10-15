package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.eirc.dao.QuittanceDao;
import org.flexpay.eirc.dao.QuittanceDaoExt;
import org.flexpay.eirc.dao.QuittanceDetailsDao;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class QuittanceServiceImpl implements QuittanceService {

	private Logger log = Logger.getLogger(getClass());

	private SessionUtils sessionUtil;
	private QuittanceDetailsDao quittanceDetailsDao;
	private QuittanceDao quittanceDao;

	private QuittanceDaoExt quittanceDaoExt;

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
	public void generateForServiceOrganisation(Stub<ServiceOrganisation> stub,
											   Date dateFrom, Date dateTill) {

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting quittances generation at " + new Date());
		}

		quittanceDaoExt.createQuittances(dateFrom, dateTill);

		if (log.isInfoEnabled()) {
			log.info("Quittances generation finished, time took: " + (System.currentTimeMillis() - time) + "ms");
		}
	}

	public String getPayer(Quittance quittance) {
		quittance = quittanceDao.read(quittance.getId());
		PersonIdentity personIdentity = quittance.getEircAccount().getPerson().getPassportIdentity();
		if (personIdentity == null) {
			quittance.getEircAccount().getPerson().getForeignPassportIdentity();
		}
		if (personIdentity == null) {
			quittance.getEircAccount().getPerson().getDefaultIdentity();
		}

		if (personIdentity != null) {
			return personIdentity.getFirstName() + " "
				   + personIdentity.getMiddleName() + " "
				   + personIdentity.getLastName();
		} else {
			return null;
		}
	}

	@NotNull
	public List<Quittance> getQuittances(Stub<ServiceOrganisation> stub, Date dateFrom, Date dateTill) {
		return quittanceDao.listQuittancesForPrinting(stub.getId(), dateFrom, dateTill);
	}

	/**
	 * Read full quittance details
	 *
	 * @param stub Quittance stub
	 * @return Quittance if found, or <code>null</code> otherwise
	 */
	public Quittance readFull(@NotNull Stub<Quittance> stub) {
		Quittance q = quittanceDao.readFull(stub.getId());

//		sessionUtil.evict(q);

		return q;
	}

	/**
	 * @param quittanceDao the quittanceDao to set
	 */
	public void setQuittanceDao(QuittanceDao quittanceDao) {
		this.quittanceDao = quittanceDao;
	}

	public void setQuittanceDetailsDao(QuittanceDetailsDao quittanceDetailsDao) {
		this.quittanceDetailsDao = quittanceDetailsDao;
	}

	public void setQuittanceDaoExt(QuittanceDaoExt quittanceDaoExt) {
		this.quittanceDaoExt = quittanceDaoExt;
	}

	public void setSessionUtil(SessionUtils sessionUtil) {
		this.sessionUtil = sessionUtil;
	}
}
