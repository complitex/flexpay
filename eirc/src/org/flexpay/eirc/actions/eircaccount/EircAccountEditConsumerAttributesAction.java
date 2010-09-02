package org.flexpay.eirc.actions.eircaccount;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.service.ConsumerAttributeTypeService;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.payments.actions.outerrequest.request.response.data.ConsumerAttributes;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.*;

public class EircAccountEditConsumerAttributesAction extends FPActionSupport {

	private EircAccount eircAccount = new EircAccount();
    private List<ConsumerAttributeTypeBase> attributeTypes = list();
    private Map<Long, Map<Long, ConsumerAttribute>> formAttributes = map();
    private Map<Long, String> attributes = treeMap();

	private SPService spService;
	private EircAccountService eircAccountService;
    private PersonService personService;
    private AddressService addressService;
    private ConsumerService consumerService;
    private ConsumerAttributeTypeService consumerAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() {

		if (eircAccount == null || eircAccount.isNew()) {
			addActionError(getText("common.error.invalid_id"));
            log.error("Incorrect eircAccount id");
			return REDIRECT_ERROR;
		}

        Stub<EircAccount> stub = stub(eircAccount);
		eircAccount = eircAccountService.readFull(stub);
		if (eircAccount == null) {
			addActionError(getText("common.object_not_selected"));
            log.error("Can't get eirc account with id {} from DB", stub.getId());
			return REDIRECT_ERROR;
		}

        attributeTypes = consumerAttributeTypeService.getByUniqueCode(ConsumerAttributes.EIRC_ATTRIBUTES);

        correctAttributes();

        if (isSubmit()) {

            try {
                updateConsumer();
            } catch (FlexPayExceptionContainer e) {
                return REDIRECT_ERROR;
            } catch (ParseException e) {
                log.error("Can't parse inputed date value");
                return REDIRECT_ERROR;
            }

            log.info("New attribute values saved");

            return REDIRECT_SUCCESS;

        }

        initData();

		return INPUT;
	}

    private void initData() {

        for (Consumer consumer : eircAccount.getConsumers()) {

            Map<Long, ConsumerAttribute> consumerAttributes = map();

            for (ConsumerAttribute attribute : consumer.getAttributes()) {
                if (!attributeTypes.contains(attribute.getType())) {
                    continue;
                }
                attributes.put(attribute.getId(), "");
                if (attribute.isDecimal() && attribute.getDecimalValue() != null) {
                    attribute.setDecimalValue(attribute.getDecimalValue().setScale(2));
                }
                consumerAttributes.put(attribute.getType().getId(), attribute);
            }

            formAttributes.put(consumer.getId(), consumerAttributes);

        }
    }

    private void correctAttributes() {
        if (attributes == null) {
            log.warn("Attributes parameter is null");
            attributes = treeMap();
        }
        Map<Long, String> newAttributes = treeMap();
        for (Consumer consumer : eircAccount.getConsumers()) {
            for (ConsumerAttribute attribute : consumer.getAttributes()) {
                if (!attributeTypes.contains(attribute.getType())) {
                    continue;
                }
                newAttributes.put(attribute.getId(), attributes.containsKey(attribute.getId()) ? attributes.get(attribute.getId()) : "");
            }
        }
        attributes = newAttributes;

    }

    private void updateConsumer() throws FlexPayExceptionContainer, ParseException {

        for (Consumer consumer : eircAccount.getConsumers()) {

            log.debug("Saving consumer with id {}", consumer.getId());

            for (ConsumerAttribute consumerAttribute : consumer.getAttributes()) {
                if (!attributeTypes.contains(consumerAttribute.getType())) {
                    continue;
                }
                consumerAttribute.setValue(attributes.get(consumerAttribute.getId()));
            }

            try {
                consumerService.save(consumer);
            } catch (FlexPayExceptionContainer e) {
                log.error("Can`t save consumer", e);
                addActionError(getText("admin.error.inner_error"));
                throw e;
            }

        }

    }

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	public String getServiceDescription(@NotNull Service service) throws Exception {
		Service persistent = spService.readFull(stub(service));
		return persistent == null ? "" : persistent.format(getLocale());
	}

    public String getAddress(@NotNull Apartment apartment) throws Exception {
        return addressService.getAddress(stub(apartment), getLocale());
    }

    public String getFIO(@NotNull Person person) {
        Person persistent = personService.readFull(stub(person));
        if (persistent == null) {
            throw new RuntimeException("Invalid person: " + person);
        }
        PersonIdentity identity = persistent.getDefaultIdentity();
        if (identity != null) {
            return identity.getLastName() + " " + identity.getFirstName() + " " + identity.getMiddleName();
        }
        throw new RuntimeException("No default identity: " + persistent);
    }

    public Map<Long, Map<Long, ConsumerAttribute>> getFormAttributes() {
        return formAttributes;
    }

    public EircAccount getEircAccount() {
		return eircAccount;
	}

	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

    public List<ConsumerAttributeTypeBase> getAttributeTypes() {
        return attributeTypes;
    }

    public Map<Long, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Long, String> attributes) {
        this.attributes = attributes;
    }

    @Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

    @Required
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Required
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Required
    public void setConsumerAttributeTypeService(ConsumerAttributeTypeService consumerAttributeTypeService) {
        this.consumerAttributeTypeService = consumerAttributeTypeService;
    }

    @Required
    public void setConsumerService(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }
}
