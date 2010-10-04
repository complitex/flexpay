package org.flexpay.eirc.service.exchange;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.QuittanceDetailsPayment;
import org.flexpay.eirc.persistence.QuittancePayment;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.service.ConsumerAttributeTypeService;
import org.flexpay.eirc.service.QuittancePaymentService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.payments.actions.outerrequest.request.GetDebtInfoRequest;
import org.flexpay.payments.actions.outerrequest.request.GetQuittanceDebtInfoRequest;
import org.flexpay.payments.actions.outerrequest.request.response.data.ConsumerAttributes;
import org.flexpay.payments.actions.outerrequest.request.response.data.QuittanceInfo;
import org.flexpay.payments.actions.outerrequest.request.response.data.ServiceDetails;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.TranslationUtil.getTranslation;

public class QuittanceInfoBuilder {

    private Logger log = LoggerFactory.getLogger(getClass());

    private QuittanceService quittanceService;
    private QuittanceNumberService quittanceNumberService;
    private MasterIndexService masterIndexService;
    private ApartmentService apartmentService;
    private QuittancePaymentService quittancePaymentService;
    private ConsumerAttributeTypeService attributeTypeService;
    private SPService spService;

    @Nullable
    public QuittanceInfo buildInfo(Stub<Quittance> stub, GetQuittanceDebtInfoRequest request) throws Exception {

        List<QuittanceDetails> quittanceDetailses;
        QuittanceInfo info = new QuittanceInfo();

        Locale locale = request.getLocale();

        Quittance q = quittanceService.readFull(stub);
        if (q == null) {
            return null;
        }

        info.setQuittanceNumber(quittanceNumberService.getNumber(q));
        info.setAccountNumber(q.getAccountNumber());
        info.setCreationDate(q.getCreationDate());
        info.setDateFrom(q.getDateFrom());
        info.setDateTill(q.getDateTill());
        info.setOrderNumber(q.getOrderNumber());

        ConsumerInfo consumerInfo = q.getEircAccount().getConsumerInfo();
        Apartment apartment = q.getEircAccount().getApartment();

        if (apartment != null) {
            Stub<Apartment> apartmentStub = stub(apartment);
            info.setApartmentMasterIndex(masterIndexService.getMasterIndex(apartment));
            setAddress1(info, apartmentStub, locale);
        } else {
            Iterator<Consumer> it = consumerInfo.getConsumers().iterator();
            if (it.hasNext()) {
                Consumer consumer = it.next();
                setAddress1(info, consumer.getApartmentStub(), locale);
            }
        }

        Person person = q.getEircAccount().getPerson();
        if (person != null) {
            info.setPersonMasterIndex(masterIndexService.getMasterIndex(person));
        } else {
            info.setPersonFirstName(consumerInfo.getFirstName());
            info.setPersonMiddleName(consumerInfo.getMiddleName());
            info.setPersonLastName(consumerInfo.getLastName());
        }

        List<QuittancePayment> payments = quittancePaymentService.getQuittancePayments(stub(q));

        info.setTotalToPay(getTotalPayable(q));
        info.setTotalPayed(getTotalPayed(q, payments));

        quittanceDetailses = q.getQuittanceDetails();

        // prepare sum to pay
        if (log.isDebugEnabled()) {
            log.debug("Total detailses: {}", quittanceDetailses.size());
        }
        for (QuittanceDetails details : quittanceDetailses) {

            Consumer consumer = details.getConsumer();
            Service service = consumer.getService();
            log.debug("Building quittanceDetails for service {}", service);

            ServiceDetails serviceDetails = new ServiceDetails();
            serviceDetails.setServiceId(service.getServiceType().getId());
            serviceDetails.setServiceName(getTranslation(service.getDescriptions(), locale).getName());
            serviceDetails.setAmount(details.getAmount().setScale(2));
            serviceDetails.setServiceProviderAccount(consumer.getExternalAccountNumber());
            serviceDetails.setIncomingBalance(details.getIncomingBalance());
            serviceDetails.setOutgoingBalance(details.getOutgoingBalance());
            serviceDetails.setServiceMasterIndex(masterIndexService.getMasterIndex(service));
            serviceDetails.setExpence(details.getExpence().setScale(2));
            serviceDetails.setRate(details.getRate().setScale(2));
            serviceDetails.setRecalculation(details.getRecalculation().setScale(2));
            serviceDetails.setBenifit(details.getBenifit().setScale(2));
            serviceDetails.setSubsidy(details.getSubsidy().setScale(2));
            serviceDetails.setPayment(details.getPayment().setScale(2));
            serviceDetails.setPayed(getPayedSum(details, payments).setScale(2));
            serviceDetails.setAttributes(getConsumerAttributes(consumer));

            ConsumerInfo cinfo = consumer.getConsumerInfo();
            serviceDetails.setPersonFirstName(cinfo.getFirstName());
            serviceDetails.setPersonMiddleName(cinfo.getMiddleName());
            serviceDetails.setPersonLastName(cinfo.getLastName());
            //TODO: Пока что непонятно откуда брать apartment, в темповом режиме берем из consumer'а
            setAddress2(serviceDetails, consumer.getApartmentStub(), locale);
//            serviceDetails.setRoomNumber("");

            info.addServiceDetails(serviceDetails);
        }

        log.debug("QuittanceInfo: {}", info);

        return info;
    }

    public List<ServiceDetails> buildServiceDetails(Stub<Quittance> stub, GetDebtInfoRequest request) throws Exception {

        List<QuittanceDetails> quittanceDetailses = quittanceService.getQuittanceDetailsByQuittanceId(stub);
        List<ServiceDetails> serviceDetailses = list();

        if (log.isDebugEnabled()) {
            log.debug("Total detailses: {}", quittanceDetailses.size());
        }
        for (QuittanceDetails details : quittanceDetailses) {

            Consumer consumer = details.getConsumer();
            Service service = consumer.getService();
            log.debug("Building quittanceDetails for service {}", service);

            ServiceDetails serviceDetails = new ServiceDetails();
            serviceDetails.setServiceId(service.getServiceType().getId());
            serviceDetails.setServiceName(getTranslation(service.getDescriptions(), request.getLocale()).getName());
            serviceDetails.setAmount(details.getAmount().setScale(2));
            serviceDetails.setServiceProviderAccount(consumer.getExternalAccountNumber());

            ConsumerInfo cinfo = consumer.getConsumerInfo();
            serviceDetails.setPersonFirstName(cinfo.getFirstName());
            serviceDetails.setPersonMiddleName(cinfo.getMiddleName());
            serviceDetails.setPersonLastName(cinfo.getLastName());
            //TODO: Пока что непонятно откуда брать apartment, в темповом режиме берем из consumer'а
            setAddress2(serviceDetails, consumer.getApartmentStub(), request.getLocale());
//            serviceDetails.setRoomNumber("");

            serviceDetailses.add(serviceDetails);
        }

        return serviceDetailses;
    }

    private void setAddress2(ServiceDetails serviceDetails, Stub<Apartment> apartmentStub, Locale locale) throws FlexPayException {

        Apartment apartment = apartmentService.readFullWithHierarchy(apartmentStub);
        if (apartment == null) {
            log.error("Can't get apartment with id {} from DB", apartmentStub.getId());
            throw new FlexPayException("Internal error. Can't get apartment from DB");
        }

        serviceDetails.setApartmentNumber(apartment.getNumber());

        BuildingAddress buildingAddress = apartment.getBuilding().getDefaultBuildings();
        serviceDetails.setBuildingBulk(buildingAddress.getBulk());
        serviceDetails.setBuildingNumber(buildingAddress.getNumber());

        Street street = buildingAddress.getStreet();
        serviceDetails.setStreetType(getTranslation(street.getCurrentType().getTranslations(), locale).getShortName());
        serviceDetails.setStreetName(getTranslation(street.getCurrentName().getTranslations(), locale).getName());

        Town town = street.getTown();
        serviceDetails.setTownType(getTranslation(town.getCurrentType().getTranslations(), locale).getShortName());
        serviceDetails.setTownName(getTranslation(town.getCurrentName().getTranslations(), locale).getName());

        Region region = town.getRegion();
        serviceDetails.setRegion(getTranslation(region.getCurrentName().getTranslations(), locale).getName());

        Country country = region.getCountry();
        serviceDetails.setCountry(getTranslation(country.getTranslations(), locale).getName());

    }

    private void setAddress1(QuittanceInfo info, Stub<Apartment> apartmentStub, Locale locale) throws FlexPayException {

        Apartment apartment = apartmentService.readFullWithHierarchy(apartmentStub);
        if (apartment == null) {
            log.error("Can't get apartment with id {} from DB", apartmentStub.getId());
            throw new FlexPayException("Internal error. Can't get apartment from DB");
        }

        info.setApartmentNumber(apartment.getNumber());

        BuildingAddress buildingAddress = apartment.getBuilding().getDefaultBuildings();
        info.setBuildingBulk(buildingAddress.getBulk());
        info.setBuildingNumber(buildingAddress.getNumber());

        Street street = buildingAddress.getStreet();
        info.setStreetType(getTranslation(street.getCurrentType().getTranslations(), locale).getShortName());
        info.setStreetName(getTranslation(street.getCurrentName().getTranslations(), locale).getName());

        Town town = street.getTown();
        info.setTownType(getTranslation(town.getCurrentType().getTranslations(), locale).getShortName());
        info.setTownName(getTranslation(town.getCurrentName().getTranslations(), locale).getName());

        Region region = town.getRegion();
        info.setRegion(getTranslation(region.getCurrentName().getTranslations(), locale).getName());

        Country country = region.getCountry();
        info.setCountry(getTranslation(country.getTranslations(), locale).getName());

    }

    public List<ServiceDetails.ServiceAttribute> getConsumerAttributes(Consumer consumer) {
        List<ServiceDetails.ServiceAttribute> attrs = list();
        for (String attrCode : ConsumerAttributes.PAYMENT_ATTRIBUTES) {
            ConsumerAttributeTypeBase type = attributeTypeService.readByCode(attrCode);
            if (type == null) {
                throw new IllegalStateException("Cannot find attribute type by code: " + attrCode);
            }
            ConsumerAttribute attribute = consumer.getAttribute(type);
            if (attribute == null) {
                continue;
            }
            attrs.add(new ServiceDetails.ServiceAttribute(attrCode, String.valueOf(attribute.value())));
        }

        return attrs;
    }

    public BigDecimal getTotalPayed(Quittance quittance, List<QuittancePayment> payments) {

        BigDecimal sum = new BigDecimal("0.00");
        for (QuittanceDetails details : quittance.getQuittanceDetails()) {
            if (getService(details).isNotSubservice()) {
                BigDecimal sumPayed = getPayedSum(details, payments);
                sum = sum.add(sumPayed);
            }
        }

        return sum;
    }

    @NotNull
    public BigDecimal getPayedSum(QuittanceDetails qd, List<QuittancePayment> payments) {
        BigDecimal sum = new BigDecimal("0.00");
        for (QuittancePayment payment : payments) {
            QuittanceDetailsPayment detailsPayment = payment.getPayment(qd);
            if (detailsPayment != null) {
                sum = sum.add(detailsPayment.getAmount());
            }
        }

        return sum;
    }

    public BigDecimal getTotalPayable(Quittance quittance) {

        BigDecimal total = new BigDecimal("0.00");

        for (QuittanceDetails qd : quittance.getQuittanceDetails()) {
            if (getService(qd).isNotSubservice()) {
                total = total.add(qd.getOutgoingBalance());
            }
        }

        return total;
    }

    private Service getService(QuittanceDetails qd) {
        Consumer consumer = qd.getConsumer();
        return spService.readFull(consumer.getServiceStub());
    }

    @Required
    public void setQuittanceService(QuittanceService quittanceService) {
        this.quittanceService = quittanceService;
    }

    @Required
    public void setQuittanceNumberService(QuittanceNumberService quittanceNumberService) {
        this.quittanceNumberService = quittanceNumberService;
    }

    @Required
    public void setMasterIndexService(MasterIndexService masterIndexService) {
        this.masterIndexService = masterIndexService;
    }

    @Required
    public void setApartmentService(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @Required
    public void setQuittancePaymentService(QuittancePaymentService quittancePaymentService) {
        this.quittancePaymentService = quittancePaymentService;
    }

    @Required
    public void setAttributeTypeService(ConsumerAttributeTypeService attributeTypeService) {
        this.attributeTypeService = attributeTypeService;
    }

    @Required
    public void setSpService(SPService spService) {
        this.spService = spService;
    }
}
