package org.flexpay.ab.service;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.impl.AbTestTownTypeUtil;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestTownTypeService extends AbSpringBeanAwareTestCase {
    @Autowired
    @Qualifier ("townTypeService")
	private TownTypeService townTypeService;

    @Autowired
    @Qualifier ("abTestTownTypeUtil")
	private AbTestTownTypeUtil townTypeUtil;

    @Test (expected = FlexPayExceptionContainer.class)
    public void testSimpleCreate() throws Exception {
        TownType newType = new TownType();
        townTypeService.create(newType);
    }

    @Test
    public void testCreateAndDisable() throws Exception {
        TownType newType = townTypeUtil.create("town1", "t1");
        assertNotNull("Town type did not create", newType);
        assertEquals("Status is not active", TownType.STATUS_ACTIVE, newType.getStatus());

        townTypeService.disable(CollectionUtils.list(newType.getId()));

        TownType updatedType = townTypeService.readFull(Stub.stub(newType));
        assertEquals("Status is not disabled", TownType.STATUS_DISABLED, updatedType.getStatus());
        assertFalse("Found disabled town", townTypeService.getEntities().contains(updatedType));
    }

    @Test
    public void testCreateAndDisable2() throws Exception {
        TownType newType1 = townTypeUtil.create("town1", "t1");
        assertNotNull("Town type did not create", newType1);
        TownType newType2 = townTypeUtil.create("town2", "t2");
        assertNotNull("Town type did not create", newType2);

        List<TownType> types = townTypeService.getEntities();
        assertTrue("Get all types", types.size() >= 2);
        assertTrue("Did not find town1", types.contains(newType1));
        assertTrue("Did not find town2", types.contains(newType2));

        townTypeService.disable(CollectionUtils.list(newType1.getId(), newType2.getId()));

        types = townTypeService.getEntities();
        assertFalse("Found town1", types.contains(newType1));
        assertFalse("Found town2", types.contains(newType2));
    }

    @Test
    public void testInitFilter() throws FlexPayException {
        //create town types with language "ru" (default)
        TownType newType1 = townTypeUtil.create("town3", "t3");
        assertNotNull("Town type did not create", newType1);
        TownType newType2 = townTypeUtil.create("town4", "t4");
        assertNotNull("Town type did not create", newType2);

        Language langRU = newType1.getDefaultTranslation().getLang();
        //check filter with default language "ru"
        TownTypeFilter filter = townTypeService.initFilter(null, langRU.getLocale());
        assertNotNull("Invalid filter", filter);
        assertNotNull("Invalid selected id. It is null", filter.getSelectedId());
        assertTrue("Filter do not content town3", filter.getNames().contains(newType1.getTranslation(langRU)));
        assertTrue("Filter do not content town4", filter.getNames().contains(newType2.getTranslation(langRU)));

        //add "en" language name (it is not default) and check value in filter
        TownTypeTranslation translation = townTypeUtil.addTranslation(newType1, "town3en", "t3en", Locale.US.getLanguage());
        assertNotNull("Translation did not add", translation);

        filter = townTypeService.initFilter(null, Locale.US);
        assertNotNull("Invalid filter", filter);
        assertFalse("Filter content town3 ru", filter.getNames().contains(newType1.getTranslation(langRU)));
        assertTrue("Filter do not content town4 ru", filter.getNames().contains(newType2.getTranslation(langRU)));
        assertTrue("Filter do not content town3 en", filter.getNames().contains(newType1.getTranslation(translation.getLang())));

        //delete town types and check it
        townTypeUtil.delete(newType1);
        townTypeUtil.delete(newType2);

        filter = townTypeService.initFilter(null, langRU.getLocale());
        assertNotNull("Invalid filter", filter);
        assertFalse("Filter content town3", filter.getNames().contains(newType1.getTranslation(langRU)));
        assertFalse("Filter content town4", filter.getNames().contains(newType2.getTranslation(langRU)));
    }
}
