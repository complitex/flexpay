package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Book;

public interface BookDao {

	/**
	 * Create Book
	 *
	 * @param book Raw book object
	 */
	void create(Book book);

}
