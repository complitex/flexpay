package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeEnum;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeEnumValue;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeSimple;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestConsumerAttributeTypeService extends EircSpringBeanAwareTestCase {

	@Autowired
	private ConsumerAttributeTypeService attributeTypeService;

	public static final Stub<ConsumerAttributeTypeBase> ATTR_ERC_ACCOUNT = new Stub<ConsumerAttributeTypeBase>(1L);
	public static final Stub<ConsumerAttributeTypeBase> ATTR_BOOK_COLOR = new Stub<ConsumerAttributeTypeBase>(2L);
	public static final Stub<ConsumerAttributeTypeBase> ATTR_RAINBOW_REMAINDER = new Stub<ConsumerAttributeTypeBase>(3L);

	@Test
	public void testReadFullTypes() {

		ConsumerAttributeTypeBase type1 = attributeTypeService.readFull(ATTR_ERC_ACCOUNT);
		assertNotNull("type1 find failed", type1);
		assertTrue("Invalid type1 ", type1 instanceof ConsumerAttributeTypeSimple);

		ConsumerAttributeTypeBase type2 = attributeTypeService.readFull(ATTR_BOOK_COLOR);
		assertNotNull("type2 find failed", type2);
		assertTrue("Invalid type2 ", type2 instanceof ConsumerAttributeTypeEnum);

		ConsumerAttributeTypeBase type3 = attributeTypeService.readFull(ATTR_RAINBOW_REMAINDER);
		assertNotNull("type2 find failed", type3);
		assertTrue("Invalid type3 ", type3 instanceof ConsumerAttributeTypeEnum);
	}

	@Test
	public void testAddEnumValue() throws Exception {

		ConsumerAttributeTypeEnum type = (ConsumerAttributeTypeEnum) attributeTypeService.readFull(ATTR_BOOK_COLOR);
		assertNotNull("type find failed", type);

		boolean hasAttribute = false;
		Collection<ConsumerAttributeTypeEnumValue> values = type.getValues();
		for (ConsumerAttributeTypeEnumValue value : values) {
			if (value.isString() && "RED".equals(value.getStringValue())) {
				hasAttribute = true;
				break;
			}
		}

		if (!hasAttribute) {
			ConsumerAttributeTypeEnumValue enumValue = new ConsumerAttributeTypeEnumValue();
			enumValue.setStringValue("RED");
			type.addValue(enumValue);
		}

		attributeTypeService.update(type);
	}

	@Test (expected = FlexPayExceptionContainer.class)
	public void testAddDuplicateEnumValue() throws Exception {

		ConsumerAttributeTypeEnum type = (ConsumerAttributeTypeEnum) attributeTypeService.readFull(ATTR_BOOK_COLOR);
		assertNotNull("type find failed", type);

		ConsumerAttributeTypeEnumValue enumValue = new ConsumerAttributeTypeEnumValue();
		enumValue.setStringValue("BLUE");
		type.addValue(enumValue);

		ConsumerAttributeTypeEnumValue duplicate = new ConsumerAttributeTypeEnumValue();
		duplicate.setStringValue("BLUE");
		type.addValue(duplicate);

		attributeTypeService.update(type);
	}
}
