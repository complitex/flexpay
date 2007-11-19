package org.flexpay.ab.actions;

import org.apache.log4j.Logger;
import org.flexpay.ab.dao.BookDao;
import org.flexpay.ab.persistence.Book;

import java.util.Date;

public class TestAction {

	private static Logger log = Logger.getLogger(TestAction.class);

	private BookDao bookDao;

	public String execute() throws Exception {

		Book book = new Book();
		book.setTitle("Test book: " + new Date());

		log.warn("Start Saving book");

//		bookDao.create(book);

		log.warn("End Saving  book");

		return "success";
	}

	public void setBookDao(BookDao bookDao) {

		log.error("Setting BookDao: " + bookDao.getClass().getName());

		this.bookDao = bookDao;
	}

	static {
		log.info("Test action loaded");
	}

	{
		log.info("Test action created!");
	}
}
