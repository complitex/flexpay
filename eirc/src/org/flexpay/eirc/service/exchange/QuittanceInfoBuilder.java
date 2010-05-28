package org.flexpay.eirc.service.exchange;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
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
import org.flexpay.payments.actions.request.data.DebtsRequest;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.quittance.ConsumerAttributes;
import org.flexpay.payments.persistence.quittance.QuittanceInfo;
import org.flexpay.payments.persistence.quittance.ServiceDetails;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.TranslationUtil.getTranslation;

public class QuittanceInfoBuilder {

    private Logger log = LoggerFactory.getLogger(getClass());

    private QuittanceService quittanceService;
    private QuittanceNumberService quittanceNumberService;
    private MasterIndexService masterIndexService;
    private ApartmentService apartmentService;
    private BuildingService buildingService;
    private StreetService streetService;
    private TownService townService;
    private RegionService regionService;
    private CountryService countryService;
    private QuittancePaymentService quittancePaymentService;
    private ConsumerAttributeTypeService attributeTypeService;
    private SPService spService;

    @Nullable
    public QuittanceInfo buildInfo(Stub<Quittance> stub, int infoType) throws Exception {

        List<QuittanceDetails> quittanceDetailses;
        List<QuittancePayment> payments = list();

        QuittanceInfo info = new QuittanceInfo();

        log.debug("infoType = {}", infoType);

        if (infoType == DebtsRequest.SEARCH_QUITTANCE_DEBT_REQUEST) {

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
                setAddress1(info, apartmentStub);
            } else {
                Iterator<Consumer> it = consumerInfo.getConsumers().iterator();
                if (it.hasNext()) {
                    Consumer consumer = it.next();
                    setAddress1(info, consumer.getApartmentStub());
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

            payments = quittancePaymentService.getQuittancePayments(stub(q));

            info.setTotalToPay(getTotalPayable(q));
            info.setTotalPayed(getTotalPayed(q, payments));

            quittanceDetailses = q.getQuittanceDetails();

        } else {
            quittanceDetailses = quittanceService.getQuittanceDetailsByQuittanceId(stub);
        }

        List<ServiceDetails> detailses = list();
        // prepare summs to pay
        if (log.isDebugEnabled()) {
            log.debug("Total detailses: {}", quittanceDetailses.size());
        }
        for (QuittanceDetails details : quittanceDetailses) {

            Consumer consumer = details.getConsumer();
            Service service = consumer.getService();

            ServiceDetails serviceDetails = new ServiceDetails();
            // Exchange serviceId by serviceTypeId in responces
            // serviceDetails.setServiceId(service.getId());
            serviceDetails.setServiceId(service.getServiceType().getId());
            serviceDetails.setServiceName(getTranslation(service.getDescriptions()).getName());
            serviceDetails.setAmount(details.getAmount().setScale(2));
            serviceDetails.setServiceProviderAccount(consumer.getAttribute(attributeTypeService.readByCode(ConsumerAttributes.ATTR_ERC_ACCOUNT)).getStringValue());

            if (infoType == DebtsRequest.SEARCH_QUITTANCE_DEBT_REQUEST) {
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
            }

            ConsumerInfo cinfo = consumer.getConsumerInfo();
            serviceDetails.setFirstName(cinfo.getFirstName());
            serviceDetails.setMiddleName(cinfo.getMiddleName());
            serviceDetails.setLastName(cinfo.getLastName());
            //TODO: Пока что непонятно откуда брать apartment, в темповом режиме берем из consumer'а
            setAddress2(serviceDetails, consumer.getApartmentStub());
//            serviceDetails.setRoomNumber("");

            detailses.add(serviceDetails);
        }

        info.setDetailses(detailses.toArray(new ServiceDetails[detailses.size()]));

        log.debug("QuittanceInfo: {}", info);

        return info;
    }

    private void setAddress2(ServiceDetails serviceDetails, Stub<Apartment> apartmentStub) {
        Apartment apartment = apartmentService.readFull(apartmentStub);
        serviceDetails.setApartmentNumber(apartment.getNumber());

        Building building = buildingService.readFull(apartment.getBuildingStub());
        BuildingAddress buildingAddress = buildingService.readFullAddress(stub(building.getDefaultBuildings()));
        serviceDetails.setBuildingBulk(buildingAddress.getBulk());
        serviceDetails.setBuildingNumber(buildingAddress.getNumber());

        Street street = streetService.readFull(buildingAddress.getStreetStub());
        serviceDetails.setStreetType(getTranslation(street.getCurrentType().getTranslations()).getShortName());
        serviceDetails.setStreetName(getTranslation(street.getCurrentName().getTranslations()).getName());

        Town town = townService.readFull(street.getTownStub());
        serviceDetails.setTownType(getTranslation(town.getCurrentType().getTranslations()).getShortName());
        serviceDetails.setTownName(getTranslation(town.getCurrentName().getTranslations()).getName());

        Region region = regionService.readFull(town.getRegionStub());
        serviceDetails.setRegion(getTranslation(region.getCurrentName().getTranslations()).getName());

        Country country = countryService.readFull(region.getCountryStub());
        serviceDetails.setCountry(getTranslation(country.getTranslations()).getName());

    }

    private void setAddress1(QuittanceInfo info, Stub<Apartment> apartmentStub) {
        Apartment apartment = apartmentService.readFull(apartmentStub);
        info.setApartmentNumber(apartment.getNumber());

        Building building = buildingService.readFull(apartment.getBuildingStub());
        BuildingAddress buildingAddress = buildingService.readFullAddress(stub(building.getDefaultBuildings()));
        info.setBuildingBulk(buildingAddress.getBulk());
        info.setBuildingNumber(buildingAddress.getNumber());

        Street street = streetService.readFull(buildingAddress.getStreetStub());
        info.setStreetType(getTranslation(street.getCurrentType().getTranslations()).getShortName());
        info.setStreetName(getTranslation(street.getCurrentName().getTranslations()).getName());

        Town town = townService.readFull(street.getTownStub());
        info.setTownType(getTranslation(town.getCurrentType().getTranslations()).getShortName());
        info.setTownName(getTranslation(town.getCurrentName().getTranslations()).getName());

        Region region = regionService.readFull(town.getRegionStub());
        info.setRegion(getTranslation(region.getCurrentName().getTranslations()).getName());

        Country country = countryService.readFull(region.getCountryStub());
        info.setCountry(getTranslation(country.getTranslations()).getName());

    }

    public ServiceDetails.ServiceAttribute[] getConsumerAttributes(Consumer consumer) {
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

        return attrs.toArray(new ServiceDetails.ServiceAttribute[attrs.size()]);
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
    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

    @Required
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
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
