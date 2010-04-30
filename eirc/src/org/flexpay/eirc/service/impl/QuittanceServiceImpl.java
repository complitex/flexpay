package org.flexpay.eirc.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.EircAccountDao;
import org.flexpay.eirc.dao.QuittanceDao;
import org.flexpay.eirc.dao.QuittanceDaoExt;
import org.flexpay.eirc.dao.QuittanceDetailsDao;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
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
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

@Transactional (readOnly = true)
public class QuittanceServiceImpl implements QuittanceService {

	private Logger log = LoggerFactory.getLogger(getClass());

    private EircAccountDao eircAccountDao;
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
    @Override
	public void save(QuittanceDetails details) throws FlexPayExceptionContainer {
		if (details.isNew()) {
			details.setId(null);
			quittanceDetailsDao.create(details);
		} else {
			quittanceDetailsDao.update(details);
		}
	}

	@Transactional (readOnly = false)
    @Override
	public void generateForServiceOrganization(QuittanceDaoExt.CreateQuittancesOptions options) {

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting quittances generation at {}", new Date());
		}

		quittanceDaoExt.createQuittances(options);

		if (log.isInfoEnabled()) {
			log.info("Quittances generation finished, time took: {} ms", System.currentTimeMillis() - time);
		}
	}

	/**
	 * Read full quittance details
	 *
	 * @param stub Quittance stub
	 * @return Quittance if found, or <code>null</code> otherwise
	 */
    @Override
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
    @Override
	public Quittance findByNumber(@NotNull String quittanceNumber) throws FlexPayException {

		QuittanceNumberService.QuittanceNumberInfo info = quittanceNumberService.parseInfo(quittanceNumber);
		List<Quittance> quittances = quittanceDao.findQuittanceByNumber(
				info.getAccountNumber(), info.getMonth(), info.getNumber());
		if (quittances.isEmpty()) {
			return null;
		}
		if (quittances.size() > 1) {
			throw new FlexPayException("Duplicate quittances?", "eirc.error.quittance.duplicates", quittanceNumber);
		}

		return quittances.get(0);
	}

	/**
	 * Find quittance for account for current open period
	 *
	 * @param stub  account stub to get quittance for
	 * @param pager Page
	 * @return list of quittance in current open period
	 */
	@NotNull
    @Override
	public List<Quittance> getLatestAccountQuittances(@NotNull Stub<EircAccount> stub, Page<Quittance> pager) {
		return quittanceDao.findAccountQuittances(stub.getId(), pager);
	}

    @NotNull
    @Override
    public List<Quittance> getQuittancesByEircAccounts(@NotNull List<Consumer> consumers) {
        Set<Long> eircAccountIds = set();
        for (Consumer consumer : consumers) {
            eircAccountIds.add(consumer.getEircAccountStub().getId());
        }
        return quittanceDao.findQuittances(eircAccountIds);
    }

    @NotNull
    @Override
    public List<Quittance> getQuittancesByApartments(@NotNull List<Consumer> consumers) {
        Set<Long> apartmentIds = set();
        for (Consumer consumer : consumers) {
            apartmentIds.add(consumer.getApartmentStub().getId());
        }
        List<EircAccount> accounts = eircAccountDao.findByApartments(apartmentIds);
        if (log.isDebugEnabled()) {
            log.debug("Found {} eirc accounts by {} apartmentIds", accounts.size(), apartmentIds.size());
        }
        Set<Long> eircAccountIds = set();
        for (EircAccount account : accounts) {
            eircAccountIds.add(account.getId());
        }
        return quittanceDao.findQuittances(eircAccountIds);
    }

    @NotNull
    @Override
    public List<QuittanceDetails> getQuittanceDetailsByQuittanceId(@NotNull Stub<Quittance> stub) {
        return quittanceDetailsDao.findByQuittanceId(stub.getId());
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

    @Required
    public void setEircAccountDao(EircAccountDao eircAccountDao) {
        this.eircAccountDao = eircAccountDao;
    }
}
