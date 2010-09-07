package org.flexpay.eirc.actions.eircaccount;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.service.ConsumerAttributeTypeService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.payments.actions.outerrequest.request.response.data.ConsumerAttributes;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;

public class EircAccountViewAction extends EircAccountAction {

    private Map<Long, List<ConsumerAttribute>> consumerAttributes = map();
    private List<ConsumerAttributeTypeBase> attributeTypes;

	private SPService spService;
	private EircAccountService eircAccountService;
    private PersonService personService;
    private AddressService addressService;
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

        for (Consumer consumer : eircAccount.getConsumers()) {
            List<ConsumerAttribute> attrs = list();
            for (ConsumerAttributeTypeBase type : attributeTypes) {
                boolean found = false;
                for (ConsumerAttribute attribute : consumer.getAttributes()) {
                    if (attribute.getType().getId().equals(type.getId())) {
                        if (attribute.isDecimal() && attribute.getDecimalValue() != null) {
                            attribute.setDecimalValue(attribute.getDecimalValue().setScale(2));
                        }
                        attrs.add(attribute);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    attrs.add(new ConsumerAttribute());
                }
            }
            consumerAttributes.put(consumer.getId(), attrs);
        }

		return SUCCESS;
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
		return REDIRECT_ERROR;
	}

	public String getServiceDescription(@NotNull Service service) throws Exception {
		Service persistent = spService.readFull(stub(service));
		return persistent == null ? "" : persistent.format(getLocale());
	}

    public String getAddress(@NotNull Apartment apartment) throws Exception {
        return addressService.getAddress(stub(apartment), getLocale());
    }

    public Map<Long, List<ConsumerAttribute>> getConsumerAttributes() {
        return consumerAttributes;
    }

    public List<ConsumerAttributeTypeBase> getAttributeTypes() {
        return attributeTypes;
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

}
