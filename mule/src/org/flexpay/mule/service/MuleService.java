package org.flexpay.mule.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.JpaSetService;
import org.flexpay.mule.Request;

public interface MuleService extends JpaSetService {

    void processApartment(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processBuildingAddress(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processBuilding(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processStreet(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processStreetType(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processDistrict(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processTownType(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processTown(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processRegion(Request request) throws FlexPayExceptionContainer, FlexPayException;

    void processCountry(Request request) throws FlexPayExceptionContainer;

}
