package org.flexpay.eirc.service.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.dao.ConsumerAttributeTypeDao;
import org.flexpay.eirc.dao.ConsumerAttributeTypeDaoExt;
import org.flexpay.eirc.persistence.consumer.*;
import org.flexpay.eirc.service.ConsumerAttributeTypeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;

@Transactional (readOnly = true)
public class ConsumerAttributeTypeServiceImpl implements ConsumerAttributeTypeService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ConsumerAttributeTypeDao attributeTypeDao;
	private ConsumerAttributeTypeDaoExt attributeTypeDaoExt;
	private SessionUtils sessionUtils;

	@NotNull
	@Override
	public ConsumerAttributeTypeBase newInstance() {
		throw new IllegalStateException("Not implemented");
	}

	@NotNull
	@Override
	@Transactional(readOnly = false)
	public ConsumerAttributeTypeBase create(@NotNull ConsumerAttributeTypeBase obj) throws FlexPayExceptionContainer {
		validate(obj);
		obj.setId(null);
		attributeTypeDao.create(obj);
		return obj;
	}

	@NotNull
	@Override
	@Transactional(readOnly = false)
	public ConsumerAttributeTypeBase update(@NotNull ConsumerAttributeTypeBase obj) throws FlexPayExceptionContainer {
		validate(obj);
		attributeTypeDao.update(obj);
		return obj;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(ConsumerAttributeTypeBase type) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		boolean defaultNameFound = false;
		for (ConsumerAttributeTypeName name : type.getNames()) {
			if (name.getLang().isDefault() && StringUtils.isNotBlank(name.getName())) {
				defaultNameFound = true;
			}
		}
		if (!defaultNameFound) {
			ex.addException(new FlexPayException(
					"No default name", "common.error.no_default_lang_name"));
		}

		// validate enum values
		if (type instanceof ConsumerAttributeTypeEnum) {
			ConsumerAttributeTypeEnum typeEnum = (ConsumerAttributeTypeEnum) type;
			Set<Object> values = CollectionUtils.set();
			int order = 0;
			for (ConsumerAttributeTypeEnumValue value : typeEnum.getValues()) {
				if (value.getOrder() != order) {
					ex.addException(new FlexPayException("Invalid order",
							"error.eirc.consumer.attribute.enum.invalid_order", value.getOrder(), order));
				}
				if (value.empty()) {
					ex.addException(new FlexPayException("Empty value",
							"error.eirc.consumer.attribute.enum.blank_value"));
				}
				Object val = value.value();
				if (!values.add(val)) {
					ex.addException(new FlexPayException("Empty value",
							"error.eirc.consumer.attribute.enum.duplicate_value", val));
				}
				++order;
			}
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void disable(@NotNull Collection<Long> ids) {
		for (Long id : ids) {
			ConsumerAttributeTypeBase type = readFull(new Stub<ConsumerAttributeTypeBase>(id));
			if (type != null) {
				type.disable();
				attributeTypeDao.update(type);
			}
		}
	}

	@Override
	public ConsumerAttributeTypeBase readFull(@NotNull Stub<? extends ConsumerAttributeTypeBase> stub) {
		ConsumerAttributeTypeBase type = attributeTypeDao.read(stub.getId());
		sessionUtils.evict(type);
		if (type instanceof ConsumerAttributeTypeSimple) {
			return attributeTypeDaoExt.readFullSimpleType(stub.getId());
		} else if (type instanceof ConsumerAttributeTypeEnum) {
			return attributeTypeDaoExt.readFullEnumType(stub.getId());
		} else if (type != null) {
			log.warn("Unknown type: {}", type);
		}
		return type;
	}

    @Override
    @NotNull
    public List<ConsumerAttributeTypeBase> readFull(@NotNull Collection<Long> ids, boolean preserveOrder) {
        return attributeTypeDao.readFullCollection(ids, preserveOrder);
    }

    /**
	 * Find attribute type by unique code
	 *
	 * @param code Unique attribute type code
	 * @return Attribute type
	 */
	@Override
	public ConsumerAttributeTypeBase readByCode(String code) {
		ConsumerAttributeTypeBase type = attributeTypeDaoExt.findAtributeTypeByCode(code);
		if (type != null) {
			return readFull(stub(type));
		}
		return null;
	}

    @NotNull
    @Override
    public List<ConsumerAttributeTypeBase> getByUniqueCode(Collection<String> codes) {
        return attributeTypeDao.findAtributeTypeByCodes(codes);
    }

    @Override
	public Class<? extends ConsumerAttributeTypeBase> getType() {
		return ConsumerAttributeTypeBase.class;
	}

	@Required
	public void setAttributeTypeDao(ConsumerAttributeTypeDao attributeTypeDao) {
		this.attributeTypeDao = attributeTypeDao;
	}

	@Required
	public void setAttributeTypeDaoExt(ConsumerAttributeTypeDaoExt attributeTypeDaoExt) {
		this.attributeTypeDaoExt = attributeTypeDaoExt;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}
}
