package org.flexpay.eirc.dao.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.QuittancePacketDaoExt;
import org.flexpay.eirc.persistence.QuittancePacket;
import org.hibernate.HibernateException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class QuittancePacketDaoExtImpl extends JpaDaoSupport implements QuittancePacketDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	@NotNull
	@SuppressWarnings ({"unchecked"})
	@Override
	public List<QuittancePacket> findPackets(ArrayStack filters, final Page<QuittancePacket> pager) {

		final StringBuilder hql = new StringBuilder();
		final StringBuilder hqlCount = new StringBuilder();

		hql.append("from QuittancePacket");
		hqlCount.append("select count(*) from QuittancePacket");

		return getJpaTemplate().executeFind(new JpaCallback<List<?>>() {
			@Override
			public List<?> doInJpa(EntityManager entityManager) throws HibernateException {

				Long count = (Long) entityManager.createQuery(hqlCount.toString()).getSingleResult();
				pager.setTotalElements(count.intValue());

				return entityManager.createQuery(hql.toString())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.getResultList();
			}
		});
	}

	@NotNull
	@Override
	public Long nextPacketNumber() {
		Object[] result = (Object[]) uniqueResult((List<?>) getJpaTemplate().
                findByNamedQuery("QuittancePacket.nextPacketNumber"));

		log.debug("Next packet result: {}, {}", result);

		Long maxId = result[0] == null ? 0L : (Long) result[0];
		Long maxNumber = result[1] == null ? 0L : (Long) result[1];

		return maxId.compareTo(maxNumber) > 0 ? maxId + 1L : maxNumber + 1L;
	}
}
