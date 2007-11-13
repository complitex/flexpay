package org.flexpay.ab.dao.imp;

import org.apache.log4j.Logger;
import org.flexpay.ab.dao.BookDao;
import org.flexpay.ab.persistence.Book;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class BookDaoImpl extends HibernateDaoSupport implements BookDao {

	private static Logger log = Logger.getLogger(BookDaoImpl.class);

	@Transactional(readOnly = false)
	public void create(Book book) {

		log.warn("Persisting book: " + book.getTitle());

		try {
			getHibernateTemplate().save(book);
		} catch (DataAccessException e) {
			log.error("failed saving book: ", e);
			throw e;
		}

		log.warn("Persisted book: " + book.getId());
	}
}
