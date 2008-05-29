package org.flexpay.ab.actions.person;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.util.DateIntervalUtil;

public class SetRegistrationAction implements SessionAware {
	public static final String PERSON_SESSION_KEY = "PERSON_SESSION_KEY";
	private PersonService personService;

	private Map session;
	private Long personId;
	private Long apartmentId;
	private Date beginDate;
	private Date endDate;

	public String execute() {
		if (apartmentId == null || apartmentId.equals("")) {
			return "form";
		}
		
		if(beginDate == null) {
			beginDate = DateIntervalUtil.now();
		}
		
		personId = Long.parseLong((String) session.get(PERSON_SESSION_KEY));
		Person person = personService.read(personId);
		person.setPersonRegistration(new Apartment(apartmentId), beginDate, endDate);
		personService.update(person);

		return "success";
	}

	/**
	 * @param apartmentId
	 *            the apartmentId to set
	 */
	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}

	/**
	 * @param personService
	 *            the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
	}

	/**
	 * @return the personId
	 */
	public Long getPersonId() {
		return personId;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 * @throws ParseException 
	 */
	public void setEndDate(String endDate) throws ParseException {
		if(StringUtils.isEmpty(endDate)) {
			return;
		}
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		this.endDate = format.parse(endDate);
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 * @throws ParseException 
	 */
	public void setBeginDate(String beginDate) throws ParseException {
		if(StringUtils.isEmpty(beginDate)) {
			return;
		}
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		this.beginDate = format.parse(beginDate);
	}
}
