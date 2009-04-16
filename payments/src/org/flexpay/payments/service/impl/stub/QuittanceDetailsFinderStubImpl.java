package org.flexpay.payments.service.impl.stub;

import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsRequest;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Stub implementation of {@link org.flexpay.payments.service.QuittanceDetailsFinder}
 * <p/>
 * Returns simple hardcoded response for each request
 */
public class QuittanceDetailsFinderStubImpl implements QuittanceDetailsFinder {

	/**
	 * Returns simple hardcoded response
	 *
	 * @param request Request for quittance details
	 * @return simple hardcoded response
	 */
	@NotNull
	public QuittanceDetailsResponse findQuittance(QuittanceDetailsRequest request) {

		QuittanceDetailsResponse.QuittanceInfo[] quittanceInfos = getTestQuittanceInfos();

		QuittanceDetailsResponse response = new QuittanceDetailsResponse();
		response.setRequestId(request.getRequestId());
		response.setErrorCode(QuittanceDetailsResponse.CODE_SUCCESS);
		response.setInfos(quittanceInfos);

		return response;
	}

	private QuittanceDetailsResponse.QuittanceInfo[] getTestQuittanceInfos() {

		QuittanceDetailsResponse.QuittanceInfo quittanceInfo1 = new QuittanceDetailsResponse.QuittanceInfo();
		quittanceInfo1.setQuittanceNumber("111-111-111");
		quittanceInfo1.setDateFrom(DateUtil.now());
		quittanceInfo1.setPersonFirstName("Zhorik");
		quittanceInfo1.setPersonLastName("Vartanov");
		quittanceInfo1.setCountry("Russia");
		quittanceInfo1.setRegion("Stavropolskiy kray");
		quittanceInfo1.setTown("Pyatigorsk");
		quittanceInfo1.setStreetType("ul");
		quittanceInfo1.setStreetName("Mira");
		quittanceInfo1.setBuildingNumber("22");
		quittanceInfo1.setApartmentNumber("33");
		quittanceInfo1.setAccountNumber("111-111");

		// services for the first quittance
		QuittanceDetailsResponse.QuittanceInfo.ServiceDetails serviceDetails1 = new QuittanceDetailsResponse.QuittanceInfo.ServiceDetails();		
		serviceDetails1.setPayed(new BigDecimal("10.00"));
		serviceDetails1.setOutgoingBalance(new BigDecimal("100.00"));

		QuittanceDetailsResponse.QuittanceInfo.ServiceDetails serviceDetails2 = new QuittanceDetailsResponse.QuittanceInfo.ServiceDetails();
		serviceDetails2.setPayed(new BigDecimal("20.00"));
		serviceDetails2.setOutgoingBalance(new BigDecimal("200.00"));

		quittanceInfo1.setDetailses(new QuittanceDetailsResponse.QuittanceInfo.ServiceDetails[] { serviceDetails1, serviceDetails2 });

		quittanceInfo1.setTotalPayed(new BigDecimal("30.00"));
		quittanceInfo1.setTotalToPay(new BigDecimal("270.00"));

		return new QuittanceDetailsResponse.QuittanceInfo[]{quittanceInfo1};
	}
}
