package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.history.Diff;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestIdentityTypeHistoryBuilder extends AbSpringBeanAwareTestCase {

	@Autowired
	private IdentityTypeService service;
	@Autowired
	private IdentityTypeHistoryBuilder historyBuilder;

	@Test
	public void testPatchType() {

		IdentityType type = service.findTypeByName(IdentityType.TYPE_NAME_FIO);
		assertNotNull("FIO type not found", type);

		Diff diff = historyBuilder.diff(null, type);
		IdentityType patchedType = new IdentityType();
		historyBuilder.patch(patchedType, diff);

		List<Language> langs = getLanguages();
		for (Language lang : langs) {
			assertEquals("Invalid name patch in lang " + lang.getLangIsoCode(),
					type.getTranslation(lang), patchedType.getTranslation(lang));
		}
		assertEquals("invalid code patch", type.getTypeId(), patchedType.getTypeId());
	}
}
