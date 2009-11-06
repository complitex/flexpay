package org.flexpay.payments.actions;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.BigDecimalUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.*;
import org.flexpay.payments.actions.quittance.SearchQuittanceAction;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public abstract class PaymentOperationAction extends CashboxCookieActionSupport {

	private String actionName;

	// form data
	private QuittanceDetailsResponse.QuittanceInfo quittanceInfo = new QuittanceDetailsResponse.QuittanceInfo();
	private Map<String, BigDecimal> payments = CollectionUtils.map();
	private Map<String, String> serviceProviderAccounts = CollectionUtils.map();
	private Map<String, String> payerFios = CollectionUtils.map();
	private Map<String, String> addresses = CollectionUtils.map();
	private Map<String, String> eircAccounts = CollectionUtils.map();
	private Map<String, BigDecimal> debts = CollectionUtils.map();
	private Map<String, String> ercAccounts = CollectionUtils.map();
	private BigDecimal changeSumm;
	private BigDecimal inputSumm;
	private BigDecimal totalToPay;

	// used to save address search criteria when using search by address
	private Long apartmentId;

	private DocumentTypeService documentTypeService;
	private DocumentStatusService documentStatusService;
	private DocumentAdditionTypeService documentAdditionTypeService;
	protected OperationService operationService;
	private OperationLevelService operationLevelService;
	private OperationStatusService operationStatusService;
	private OperationTypeService operationTypeService;
	protected CashboxService cashboxService;
	private OrganizationService organizationService;
	private SPService spService;
	private ServiceProviderService serviceProviderService;


	private ApartmentService apartmentService;
	private BuildingService buildingService;
	private StreetService streetService;
	private TownService townService;
	private RegionService regionService;
	private CountryService countryService;

	protected Operation createOperation(Cashbox cashbox) throws FlexPayException {

		Operation op = new Operation();
		fillOperation(op);
		return op;
	}

	protected void fillOperation(Operation operation) throws FlexPayException {

		Cashbox cashbox = cashboxService.read(operation.getCashboxStub());

		Organization organization = cashbox.getPaymentPoint().getCollector().getOrganization();
		operation.setOperationSumm(totalToPay);
		operation.setOperationInputSumm(inputSumm);
		operation.setChange(changeSumm);
		operation.setCreationDate(new Date());
		operation.setRegisterDate(new Date());
		operation.setCreatorOrganization(organization);
		operation.setPaymentPoint(cashbox.getPaymentPoint());
		operation.setCashbox(cashbox);
		operation.setRegisterOrganization(organization);
		operation.setCreatorUserName(SecurityUtil.getUserName());
		operation.setRegisterUserName(SecurityUtil.getUserName());
		operation.setOperationStatus(operationStatusService.read(OperationStatus.REGISTERED));
		operation.setOperationLevel(operationLevelService.read(OperationLevel.AVERAGE));
		operation.setOperationType(operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT));
		operation.setCashierFio(getUserPreferences().getFullName());

		for (String serviceIndex : payments.keySet()) {

			Document document = buildDocument(serviceIndex, cashbox);

			if (StringUtils.isEmpty(operation.getAddress())) {
				operation.setAddress(document.getAddress());
				operation.setPayerFIO(StringUtils.stripToEmpty(document.getPayerFIO()));
			}

			if (!BigDecimalUtil.isZero(document.getSumm())) {
				operation.addDocument(document);
			}
		}
	}

	private Document buildDocument(String serviceFullIndex, Cashbox cashbox) throws FlexPayException {
		
		BigDecimal documentSumm = payments.get(serviceFullIndex);
		String serviceId = SearchQuittanceAction.ServiceFullIndexUtil.getServiceIdFromIndex(serviceFullIndex);
		Service service = spService.readFull(new Stub<Service>(Long.parseLong(serviceId)));
		ServiceProvider serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(service.getServiceProvider().getId()));
		Organization serviceProviderOrganization = serviceProvider.getOrganization();

		String serviceProviderAccount = serviceProviderAccounts.get(serviceFullIndex);

		Document document = new Document();
		document.setService(service);
		document.setDocumentStatus(documentStatusService.read(DocumentStatus.REGISTERED));
		document.setDocumentType(documentTypeService.read(DocumentType.CASH_PAYMENT));
		document.setSumm(documentSumm);
		document.setDebt(debts.get(serviceFullIndex));
		document.setAddress(addresses.get(serviceFullIndex));
		document.setPayerFIO(StringUtils.stripToEmpty(payerFios.get(serviceFullIndex)));
		document.setDebtorOrganization(cashbox.getPaymentPoint().getCollector().getOrganization());
		document.setDebtorId(eircAccounts.get(serviceFullIndex));
		document.setCreditorOrganization(serviceProviderOrganization);
		document.setCreditorId(serviceProviderAccount);

		DocumentAddition ercAccountAddition = new DocumentAddition();
		ercAccountAddition.setAdditionType(documentAdditionTypeService.findTypeByCode(DocumentAdditionType.CODE_ERC_ACCOUNT));
		ercAccountAddition.setDocument(document);
		ercAccountAddition.setStringValue(ercAccounts.get(serviceFullIndex));
		document.setAdditions(CollectionUtils.set(ercAccountAddition));

		if (apartmentId != null) {
			setPayerAddress(document);
		}

		setPayerName(serviceFullIndex, document);

		return document;
	}

	private void setPayerAddress(Document document) {
		Apartment apartment = apartmentService.readFull(new Stub<Apartment>(apartmentId));
		document.setApartmentNumber(apartment.getNumber());

		Building building = buildingService.readFull(apartment.getBuildingStub());
		BuildingAddress buildingAddress = buildingService.readFullAddress(stub(building.getDefaultBuildings()));
		document.setBuildingBulk(buildingAddress.getBulk());
		document.setBuildingNumber(buildingAddress.getNumber());

		Street street = streetService.readFull(buildingAddress.getStreetStub());
		document.setStreetType(getTranslation(street.getCurrentType().getTranslations()).getShortName());
		document.setStreetName(getTranslation(street.getCurrentName().getTranslations()).getName());

		Town town = townService.readFull(street.getTownStub());
		document.setTown(getTranslation(town.getCurrentName().getTranslations()).getName());

		Region region = regionService.readFull(town.getRegionStub());
		document.setRegion(getTranslation(region.getCurrentName().getTranslations()).getName());

		Country country = countryService.readFull(region.getCountryStub());
		document.setCountry(getTranslation(country.getTranslations()).getName());
	}

	private void setPayerName(String serviceFullIndex, Document document) {
		String[] pieces = payerFios.get(serviceFullIndex).split(" ");
		List<String> tokens = new ArrayList<String>();
		for (String p : pieces) {
			if (StringUtils.isNotBlank(p)) {
				tokens.add(p);
			}
		}

		if (tokens.size() > 0) {
			document.setLastName(tokens.get(0));
		}

		if (tokens.size() > 1) {
			document.setFirstName(tokens.get(1));
		}

		if (tokens.size() > 2) {
			StringBuilder middleNameBuilder = new StringBuilder();
			for (int i = 2; i < tokens.size(); i++) {
				middleNameBuilder.append(tokens.get(i));
				if (i < tokens.size() - 1) {
					middleNameBuilder.append(" ");
				}
			}
			document.setMiddleName(middleNameBuilder.toString());
		}
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public QuittanceDetailsResponse.QuittanceInfo getQuittanceInfo() {
		return quittanceInfo;
	}

	public void setQuittanceInfo(QuittanceDetailsResponse.QuittanceInfo quittanceInfo) {
		this.quittanceInfo = quittanceInfo;
	}

	public Map<String, BigDecimal> getPayments() {
		return payments;
	}

	public void setPayments(Map<String, BigDecimal> payments) {
		this.payments = payments;
	}

	public Map<String, String> getServiceProviderAccounts() {
		return serviceProviderAccounts;
	}

	public void setServiceProviderAccounts(Map<String, String> serviceProviderAccounts) {
		this.serviceProviderAccounts = serviceProviderAccounts;
	}

	public Map<String, String> getPayerFios() {
		return payerFios;
	}

	public void setPayerFios(Map<String, String> payerFios) {
		this.payerFios = payerFios;
	}

	public Map<String, String> getAddresses() {
		return addresses;
	}

	public void setAddresses(Map<String, String> addresses) {
		this.addresses = addresses;
	}

	public Map<String, String> getEircAccounts() {
		return eircAccounts;
	}

	public void setEircAccounts(Map<String, String> eircAccounts) {
		this.eircAccounts = eircAccounts;
	}

	public Map<String, BigDecimal> getDebts() {
		return debts;
	}

	public void setDebts(Map<String, BigDecimal> debts) {
		this.debts = debts;
	}

	public Map<String, String> getErcAccounts() {
		return ercAccounts;
	}

	public void setErcAccounts(Map<String, String> ercAccounts) {
		this.ercAccounts = ercAccounts;
	}

	public BigDecimal getChangeSumm() {
		return changeSumm;
	}

	public void setChangeSumm(BigDecimal changeSumm) {
		this.changeSumm = changeSumm;
	}

	public BigDecimal getInputSumm() {
		return inputSumm;
	}

	public void setInputSumm(BigDecimal inputSumm) {
		this.inputSumm = inputSumm;
	}

	public BigDecimal getTotalToPay() {
		return totalToPay;
	}

	public void setTotalToPay(BigDecimal totalToPay) {
		this.totalToPay = totalToPay;
	}

	public void setApartmentId(Long apartmentId) {
		this.apartmentId = apartmentId;
	}

	@Required
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	@Required
	public void setDocumentStatusService(DocumentStatusService documentStatusService) {
		this.documentStatusService = documentStatusService;
	}

	@Required
	public void setOperationLevelService(OperationLevelService operationLevelService) {
		this.operationLevelService = operationLevelService;
	}

	@Required
	public void setOperationStatusService(OperationStatusService operationStatusService) {
		this.operationStatusService = operationStatusService;
	}

	@Required
	public void setOperationTypeService(OperationTypeService operationTypeService) {
		this.operationTypeService = operationTypeService;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	@Required
	public void setDocumentAdditionTypeService(DocumentAdditionTypeService documentAdditionTypeService) {
		this.documentAdditionTypeService = documentAdditionTypeService;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}
	
}
