package org.flexpay.eirc.service.exchange;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.CollectionUtils;
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
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.quittance.ConsumerAttributes;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse.ServiceAttribute;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse.QuittanceInfo;
import static org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse.QuittanceInfo.ServiceDetails;

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
    public QuittanceInfo buildInfo(Stub<Quittance> stub) throws Exception {

        Quittance q = quittanceService.readFull(stub);
        if (q == null) {
            return null;
        }

        QuittanceInfo info = new QuittanceInfo();
        info.setQuittanceNumber(quittanceNumberService.getNumber(q));
        info.setAccountNumber(q.getAccountNumber());
        info.setCreationDate(q.getCreationDate());
        info.setDateFrom(q.getDateFrom());
        info.setDateTill(q.getDateTill());
        info.setOrderNumber(q.getOrderNumber());

        ConsumerInfo consumerInfo = q.getEircAccount().getConsumerInfo();

        Apartment apartment = q.getEircAccount().getApartment();
        apartment = apartment != null ? apartmentService.readFull(stub(apartment)) : null;
        if (apartment != null) {
            info.setApartmentMasterIndex(masterIndexService.getMasterIndex(apartment));
        } else {
            info.setApartmentNumber(consumerInfo.getApartmentNumber());
            info.setBuildingNumber(consumerInfo.getBuildingNumber());
            info.setBuildingBulk(consumerInfo.getBuildingBulk());
            info.setStreetName(consumerInfo.getStreetName());
            info.setStreetType(consumerInfo.getStreetTypeName());
            info.setTown(consumerInfo.getCityName());
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

        List<ServiceDetails> detailses = CollectionUtils.list();
        // prepare summs to pay
        if (log.isDebugEnabled()) {
            log.debug("Total detailses: {}", q.getQuittanceDetails().size());
        }
        for (QuittanceDetails details : q.getQuittanceDetails()) {

            Consumer consumer = details.getConsumer();
            Service service = consumer.getService();

            ServiceDetails serviceDetails = new ServiceDetails();
            serviceDetails.setServiceMasterIndex(masterIndexService.getMasterIndex(service));
            serviceDetails.setServiceProviderAccount(details.getConsumer().getExternalAccountNumber());
            serviceDetails.setIncomingBalance(details.getIncomingBalance());
            serviceDetails.setOutgoingBalance(details.getOutgoingBalance());
            serviceDetails.setAmount(details.getAmount());
            serviceDetails.setExpence(details.getExpence());
            serviceDetails.setRate(details.getRate());
            serviceDetails.setRecalculation(details.getRecalculation());
            serviceDetails.setBenifit(details.getBenifit());
            serviceDetails.setSubsidy(details.getSubsidy());
            serviceDetails.setPayment(details.getPayment());
            serviceDetails.setPayed(getPayedSumm(details, payments));

            ConsumerInfo cinfo = consumer.getConsumerInfo();
            serviceDetails.setFirstName(cinfo.getFirstName());
            serviceDetails.setMiddleName(cinfo.getMiddleName());
            serviceDetails.setLastName(cinfo.getMiddleName());
            serviceDetails.setTown(cinfo.getCityName());
            serviceDetails.setStreetName(cinfo.getStreetName());
            serviceDetails.setStreetType(cinfo.getStreetTypeName());
            serviceDetails.setBuildingNumber(cinfo.getBuildingNumber());
            serviceDetails.setBuildingBulk(cinfo.getBuildingBulk());
            serviceDetails.setApartmentNumber(cinfo.getApartmentNumber());

            serviceDetails.setAttributes(getConsumerAttributes(consumer));

            detailses.add(serviceDetails);
        }

        info.setDetailses(detailses.toArray(new ServiceDetails[detailses.size()]));

        return info;
    }

    public ServiceAttribute[] getConsumerAttributes(Consumer consumer) {
        List<ServiceAttribute> attrs = CollectionUtils.list();
        for (String attrCode : ConsumerAttributes.PAYMENT_ATTRIBUTES) {
            ConsumerAttributeTypeBase type = attributeTypeService.readByCode(attrCode);
            if (type == null) {
                throw new IllegalStateException("Cannot find attribute type by code: " + attrCode);
            }
            ConsumerAttribute attribute = consumer.getAttribute(type);
            if (attribute == null) {
                continue;
            }
            attrs.add(new ServiceAttribute(attrCode, String.valueOf(attribute.value())));
        }

        return attrs.toArray(new ServiceAttribute[attrs.size()]);
    }

    public BigDecimal getTotalPayed(Quittance quittance, List<QuittancePayment> payments) {

        BigDecimal summ = new BigDecimal("0.00");
        for (QuittanceDetails details : quittance.getQuittanceDetails()) {
            if (getService(details).isNotSubservice()) {
                BigDecimal summPayed = getPayedSumm(details, payments);
                summ = summ.add(summPayed);
            }
        }

        return summ;
    }

    @NotNull
    public BigDecimal getPayedSumm(QuittanceDetails qd, List<QuittancePayment> payments) {
        BigDecimal summ = new BigDecimal("0.00");
        for (QuittancePayment payment : payments) {
            QuittanceDetailsPayment detailsPayment = payment.getPayment(qd);
            if (detailsPayment != null) {
                summ = summ.add(detailsPayment.getAmount());
            }
        }

        return summ;
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
