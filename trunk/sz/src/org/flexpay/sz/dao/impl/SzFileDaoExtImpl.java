package org.flexpay.sz.dao.impl;

import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.sz.dao.SzFileDaoExt;
import org.hibernate.HibernateException;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import java.util.Collection;

public class SzFileDaoExtImpl extends JpaDaoSupport implements SzFileDaoExt {

	@Override
	public void updateStatus(@NotNull final Collection<Long> fileIds, @NotNull final FPFileStatus status) {

		getJpaTemplate().execute(new JpaCallback<Integer>() {
			@Override
			public Integer doInJpa(EntityManager entityManager) throws HibernateException {
				return entityManager.createQuery("update SzFile f set f.status.id = :statusId " +
										   "where f.id in (:ids)")
						.setParameter("statusId", status.getId())
						.setParameter("ids", fileIds)
						.executeUpdate();
			}
		});
	}

}
