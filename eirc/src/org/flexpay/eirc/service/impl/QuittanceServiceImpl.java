package org.flexpay.eirc.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.dao.ConsumerDao;
import org.flexpay.eirc.dao.QuittanceDao;
import org.flexpay.eirc.dao.QuittanceDaoExt;
import org.flexpay.eirc.dao.QuittanceDetailsDao;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;

@Transactional (readOnly = true)
public class QuittanceServiceImpl implements QuittanceService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private QuittanceDetailsDao quittanceDetailsDao;
	private QuittanceDao quittanceDao;
	private QuittanceDaoExt quittanceDaoExt;
	private QuittanceNumberService quittanceNumberService;
    private ConsumerDao consumerDao;

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
     * Find quittance by generated number and service type
     *
     * @param quittanceNumber Generated quittance number
     * @param serviceTypeStub service type stub
     * @return Quittance if found, or <code>null</code> otherwise
     * @throws org.flexpay.common.exception.FlexPayException
     *          if <code>quittanceNumber</code> has invalid format
     */
    @Nullable
    @Override
    public Quittance findByNumberAndServiceType(@NotNull String quittanceNumber, @NotNull Stub<ServiceType> serviceTypeStub) throws FlexPayException {

        QuittanceNumberService.QuittanceNumberInfo info = quittanceNumberService.parseInfo(quittanceNumber);
        List<Quittance> quittances = quittanceDao.findQuittanceByNumberAndServiceType(
                info.getAccountNumber(), info.getMonth(), info.getNumber(), serviceTypeStub.getId());
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
	 * @param account  account to get quittance for
     *
	 * @return list of quittance in current open period
	 */
	@NotNull
    @Override
	public List<Quittance> getLatestAccountQuittances(@NotNull EircAccount account) {
		return getLatestAccountsQuittances(list(account));
	}

    /**
     * Find quittance for account for current open period by service type
     *
     * @param account  account to get quittance for
     * @param serviceTypeStub service type stub
     *
     * @return list of quittance in current open period
     */
    @NotNull
    @Override
    public List<Quittance> getLatestAccountQuittances(@NotNull EircAccount account, @NotNull Stub<ServiceType> serviceTypeStub) {
        return getLatestAccountsQuittances(list(account), serviceTypeStub);
    }


    @NotNull
    @Override
    public List<Quittance> getLatestAccountsQuittances(@NotNull List<EircAccount> accounts) {
        Set<Long> accountIds = set();
        for (EircAccount account : accounts) {
            accountIds.add(account.getId());
        }
        return quittanceDaoExt.findQuittancesByEIRCAccounts(accountIds);
    }

    @NotNull
    @Override
    public List<Quittance> getLatestAccountsQuittances(@NotNull List<EircAccount> accounts, @NotNull Stub<ServiceType> serviceTypeStub) {
        Set<Long> accountIds = set();
        for (EircAccount account : accounts) {
            accountIds.add(account.getId());
        }
        return quittanceDaoExt.findQuittancesByEIRCAccountsAndServiceType(accountIds, serviceTypeStub.getId());
    }

    @NotNull
    @Override
    public List<Quittance> getQuittances(@NotNull List<Consumer> consumers) {
        Set<Long> consumerIds = set();
        for (Consumer consumer : consumers) {
            consumerIds.add(consumer.getId());
        }
        return quittanceDaoExt.findQuittances(consumerIds);
    }

    @NotNull
    @Override
    public List<Quittance> getQuittancesByApartments(@NotNull List<Consumer> consumers) {
        Set<Long> apartmentIds = set();
        for (Consumer consumer : consumers) {
            apartmentIds.add(consumer.getApartmentStub().getId());
        }
        List<Consumer> consumers1 = consumerDao.findConsumersByApartments(apartmentIds);
        return getQuittances(consumers1);
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
    public void setConsumerDao(ConsumerDao consumerDao) {
        this.consumerDao = consumerDao;
    }
}
