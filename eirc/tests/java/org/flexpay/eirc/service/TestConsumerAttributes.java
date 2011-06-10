package org.flexpay.eirc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestConsumerAttributes extends EircSpringBeanAwareTestCase {

	@Autowired
	private ConsumerService consumerService;
	@Autowired
	private ConsumerAttributeTypeService attributeTypeService;

	@Test
	public void testCreateNormalAttribute() throws Exception {

		Consumer consumer = consumerService.read(new Stub<Consumer>(15L));
		assertNotNull("consumer #15 not found", consumer);

		ConsumerAttributeTypeBase typeErcAcc = attributeTypeService.readFull(
				TestConsumerAttributeTypeService.ATTR_ERC_ACCOUNT);
		assertNotNull("attribute type #1 not found", typeErcAcc);

		ConsumerAttribute attr = new ConsumerAttribute();
		attr.setType(typeErcAcc);
		attr.setStringValue("1234");
		consumer.setNormalAttribute(attr);

		try {
			consumerService.save(consumer);
		} finally {
			removeConsumerAttributes(15L);
		}
	}

	private void removeConsumerAttributes(final Long id) {
		jpaTemplate.execute(new JpaCallback<Void>() {
			@Override
			public Void doInJpa(EntityManager entityManager) throws PersistenceException {
				entityManager.createQuery("delete from ConsumerAttribute where consumer.id=:ID")
						.setParameter("ID", id).executeUpdate();
				return null;
			}
		});
	}

	@Test
	public void testCreateTmpAttribute() throws Exception {

		Consumer consumer = consumerService.read(new Stub<Consumer>(16L));
		assertNotNull("consumer #15 not found", consumer);

		ConsumerAttributeTypeBase typeErcAcc = attributeTypeService.readFull(
				TestConsumerAttributeTypeService.ATTR_ERC_ACCOUNT);
		assertNotNull("attribute type #1 not found", typeErcAcc);

		ConsumerAttribute attr = new ConsumerAttribute();
		attr.setType(typeErcAcc);
		attr.setStringValue("1234");
		consumer.setTmpAttributeForDate(attr, DateUtil.parseDate("2009/07/24"));
		try {
			consumerService.save(consumer);
		} finally {
			removeConsumerAttributes(16L);
		}
	}

	@Test
	public void testReadAttributes() throws Exception {

		Consumer consumer = consumerService.read(new Stub<Consumer>(1L));
		assertNotNull("consumer #1 not found", consumer);

		ConsumerAttributeTypeBase typeErcAcc = attributeTypeService.readFull(
				TestConsumerAttributeTypeService.ATTR_ERC_ACCOUNT);
		assertNotNull("attribute type #1 not found", typeErcAcc);

		ConsumerAttribute attribute = consumer.getAttribute(typeErcAcc);
		assertNotNull("Consumer attribute not found", attribute);
		assertEquals("Expected not temporal attribute", 0, attribute.getTemporal());

		ConsumerAttributeTypeBase typeBookColor = attributeTypeService.readFull(
				TestConsumerAttributeTypeService.ATTR_BOOK_COLOR);
		assertNotNull("attribute type #2 not found", typeBookColor);

		ConsumerAttribute attrColor = consumer.getAttributeForDate(typeBookColor, DateUtil.parseDate("1990/01/01"));
		assertNotNull("Attribute color not found", attrColor);
		assertEquals("Expected temporal attribute", 1, attrColor.getTemporal());
		assertEquals("Invalid attribute value", "Красный", attrColor.getStringValue());

		ConsumerAttribute attrColor2 = consumer.getAttributeForDate(typeBookColor, DateUtil.parseDate("2010/01/01"));
		assertNotNull("Attribute color not found", attrColor2);
		assertEquals("Expected temporal attribute", 1, attrColor2.getTemporal());
		assertEquals("Invalid attribute value", "Синий", attrColor2.getStringValue());
	}

	@Test
	public void testUpdateNormalAttributes() throws Exception {

		Consumer consumer = consumerService.read(new Stub<Consumer>(1L));
		assertNotNull("consumer #1 not found", consumer);

		Consumer copy = consumer.copy();
		ConsumerAttributeTypeBase typeErcAcc = attributeTypeService.readFull(
				TestConsumerAttributeTypeService.ATTR_ERC_ACCOUNT);
		assertNotNull("attribute type #1 not found", typeErcAcc);

		ConsumerAttribute attr = new ConsumerAttribute();
		attr.setType(typeErcAcc);
		attr.setStringValue("1234");
		copy.setNormalAttribute(attr);
		consumerService.save(copy);

		copy = consumerService.read(Stub.stub(copy));
		assertNotNull("Copy not found", copy);

		ConsumerAttribute tmpAttr = copy.getAttribute(typeErcAcc);
		assertNotNull("Just set attribute not found", tmpAttr);
		assertEquals("Invalid attribute setup", "1234", tmpAttr.getStringValue());

		attr = new ConsumerAttribute();
		attr.setType(typeErcAcc);
		attr.setStringValue("1234-1234");
		copy.setNormalAttribute(attr);
		consumerService.save(copy);

		copy = consumerService.read(Stub.stub(copy));
		assertNotNull("Copy not found", copy);

		tmpAttr = copy.getAttribute(typeErcAcc);
		assertNotNull("Just set attribute not found", tmpAttr);
		assertEquals("Invalid attribute setup", "1234-1234", tmpAttr.getStringValue());
		assertEquals("Invalid normal attribute setup", 1, copy.getAttributes().size());
	}

	@Test
	public void testUpdateTmpAttributes() throws Exception {

		Consumer consumer = consumerService.read(new Stub<Consumer>(1L));
		assertNotNull("consumer #1 not found", consumer);

		Consumer copy = consumer.copy();
		ConsumerAttributeTypeBase typeErcAcc = attributeTypeService.readFull(
				TestConsumerAttributeTypeService.ATTR_RAINBOW_REMAINDER);
		assertNotNull("attribute type #1 not found", typeErcAcc);

		ConsumerAttribute attr = new ConsumerAttribute();
		attr.setType(typeErcAcc);
		attr.setStringValue("1234");
		copy.setTmpAttributeForDate(attr, DateUtil.parseDate("2000/01/01"));
		consumerService.save(copy);
		copy = consumerService.read(Stub.stub(copy));
		assertNotNull("Copy not found", copy);

		ConsumerAttribute tmpAttr = copy.getAttribute(typeErcAcc);
		assertNotNull("Just set attribute not found", tmpAttr);
		assertEquals("Tmp attribute set failed", 1, tmpAttr.getTemporal());
		assertEquals("Invalid attribute setup", "1234", tmpAttr.getStringValue());

		attr = new ConsumerAttribute();
		attr.setType(typeErcAcc);
		attr.setStringValue("1234-1234");
		copy.setTmpAttributeForDate(attr, DateUtil.parseDate("2001/01/01"));
		consumerService.save(copy);
		copy = consumerService.read(Stub.stub(copy));
		assertNotNull("Copy not found", copy);

		assertEquals("Invalid normal attribute setup", 2, copy.getAttributes().size());
		tmpAttr = copy.getAttributeForDate(typeErcAcc, DateUtil.parseDate("2000/01/01"));
		assertNotNull("Previously set attribute not found", tmpAttr);
		assertEquals("Tmp attribute set failed", 1, tmpAttr.getTemporal());
		assertEquals("Invalid attribute setup", "1234", tmpAttr.getStringValue());
		tmpAttr = copy.getAttributeForDate(typeErcAcc, DateUtil.parseDate("2001/01/01"));
		assertNotNull("Just set attribute not found", tmpAttr);
		assertEquals("Invalid attribute setup", "1234-1234", tmpAttr.getStringValue());
	}
}
