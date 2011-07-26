package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

/**
 * Modification listener that does nothing
 *
 * @param <T>
 */
public class NopeModificationListener<T extends DomainObject> implements ModificationListener<T> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MasterIndexService masterIndexService;
	private CorrectionsService correctionsService;

	/**
	 * Notify of new object created
	 *
	 * @param obj New object
	 */
    @Override
	public void onCreate(@NotNull T obj) {
		// create dummy master index
		String masterIndex = masterIndexService.getNewMasterIndex(obj);
		if (masterIndex == null) {
			log.warn("Cannot create master index for new object");
			return;
		}

		try {
			DataCorrection correction = correctionsService.getStub(
					masterIndex, obj, masterIndexService.getMasterSourceDescriptionStub());
			correctionsService.save(correction);
		} catch (IllegalArgumentException ex) {
			log.warn("Cannot determine object type code by class {}", obj.getClass());
		}
	}

	/**
	 * Notify of object update
	 *
	 * @param objOld object old version
	 * @param obj	object new version
	 */
    @Override
	public void onUpdate(@NotNull T objOld, @NotNull T obj) {
	}

	/**
	 * Notify of object delete
	 *
	 * @param obj object that was deleted
	 */
    @Override
	public void onDelete(@NotNull T obj) {
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        masterIndexService.setJpaTemplate(jpaTemplate);
        correctionsService.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

}
