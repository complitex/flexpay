package org.flexpay.ab.service;

import java.util.List;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.ObjectAlreadyExistException;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;

public interface ApartmentService {

	List<Apartment> getApartments(ArrayStack filters, Page pager);

	/**
	 * Try to find apartment by building and number
	 * 
	 * @param building
	 *            Building
	 * @param number
	 *            Apartment number
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	Apartment findApartmentStub(Building building, String number);

	/**
	 * Get apartment number
	 * 
	 * @param apartment
	 *            Apartment, possibly a stub
	 * @return Apartment number
	 * @throws FlexPayException
	 *             if apartment specified is invalid
	 */
	String getApartmentNumber(Apartment apartment) throws FlexPayException;

	/**
	 * Get building apartment belongs to
	 * 
	 * @param apartment
	 *            Apartment stub
	 * @return Building stub
	 */
	Building getBuilding(Apartment apartment);
	
	/**
	 * Read full apartment information
	 *
	 * @param id Apartment id
	 * @return Apartment instance, or <code>null</code> if not found
	 */
	Apartment readFull(Long id);
	
	
	/**
	 * Validate that given number not alredy exist in given apartment's building. If not exist then set new number for given apartment.  
	 *
	 * @param apartment Apartment
	 * @param number apartment number
	 * @throws ObjectAlreadyExistException if given number alredy exists in given apartment's building.
	 */
	void setApartmentNumber(Apartment apartment, String number) throws ObjectAlreadyExistException;

	public String getAddress(Apartment apartment) throws FlexPayException;

	/**
	 * Create a new apartment
	 *
	 * @param apartment Apartment object
	 */
	void create(Apartment apartment);
}
