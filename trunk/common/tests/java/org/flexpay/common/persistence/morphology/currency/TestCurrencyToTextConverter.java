package org.flexpay.common.persistence.morphology.currency;

import org.flexpay.common.service.CurrencyInfoService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TestCurrencyToTextConverter extends SpringBeanAwareTestCase {

	@Autowired
	private CurrencyInfoService service;

	private CurrencyToTextConverter converter = new CurrencyToTextConverterImpl();

	@Test
	public void testMoneyToText() {

		assertEquals("Incorrect", "ноль грн ноль коп", converter.toText(new BigDecimal("0.0"), service.getDefaultCurrency()));
		assertEquals("Incorrect", "одна грн ноль коп", converter.toText(new BigDecimal("1.0"), service.getDefaultCurrency()));
		assertEquals("Incorrect", "две грн одна коп", converter.toText(new BigDecimal("2.01"), service.getDefaultCurrency()));

		assertEquals("Incorrect", "одна тысяча двести тридцать четыре грн одна коп",
				converter.toText(new BigDecimal("1234.0109"), service.getDefaultCurrency()));
	}
}
