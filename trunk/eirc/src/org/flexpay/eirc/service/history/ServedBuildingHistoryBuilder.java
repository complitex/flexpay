package org.flexpay.eirc.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.bti.service.history.BtiBuildingHistoryBuilder;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.builder.ReferenceExtractor;
import org.flexpay.common.persistence.history.builder.ReferencePatcher;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.service.importexport.impl.ClassToTypeRegistryEirc;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ServedBuildingHistoryBuilder extends BtiBuildingHistoryBuilder {

	public static final int FIELD_SERVICE_ORGANIZATION_ID = ClassToTypeRegistryEirc.MODULE_BASE + 1;

	private ServiceOrganizationService serviceOrganizationService;

	/**
	 * Do any module specific diff building.
	 * <p/>
	 * Point to extension.
	 *
	 * @param b1   First building
	 * @param b2   Second building
	 * @param diff Diff to store history records in
	 */
	@Override
	protected void doInstanceDiff(@NotNull Building b1, @NotNull Building b2, @NotNull Diff diff) {
		super.doInstanceDiff(b1, b2, diff);

		buildServiceOrganizationRefDiff((ServedBuilding) b1, (ServedBuilding) b2, diff);
	}

	private void buildServiceOrganizationRefDiff(ServedBuilding p1, ServedBuilding p2, Diff diff) {

		log.debug("building service organization reference");

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<ServiceOrganization, ServedBuilding>() {
            @Override
			public ServiceOrganization getReference(ServedBuilding obj) {
				return obj.getServiceOrganization();
			}

            @Override
			public int getReferenceField() {
				return FIELD_SERVICE_ORGANIZATION_ID;
			}
		});
	}

	/**
	 * Check if module can handle field processing
	 * <p/>
	 * Point to extension.
	 *
	 * @param fieldType History record to process
	 * @return <code>true</code>
	 */
	@Override
	protected boolean supportsField(int fieldType) {
		return fieldType == FIELD_SERVICE_ORGANIZATION_ID || super.supportsField(fieldType);
	}

	/**
	 * Do any module specific patch operations.
	 * <p/>
	 * Point to extension.
	 *
	 * @param building Building to patch
	 * @param record   History record to process
	 */
	@Override
	protected void doInstancePatch(@NotNull Building building, HistoryRecord record) {
		super.doInstancePatch(building, record);
		patchServiceOrganizationReference((ServedBuilding) building, record);
	}

	private void patchServiceOrganizationReference(@NotNull ServedBuilding building, @NotNull HistoryRecord record) {
		log.debug("Patching service organization reference {}", record);

		builderHelper.patchReference(building, record, new ReferencePatcher<ServiceOrganization, ServedBuilding>() {
            @Override
			public Class<ServiceOrganization> getType() {
				return ServiceOrganization.class;
			}

            @Override
			public void setReference(ServedBuilding obj, Stub<ServiceOrganization> ref) {
				ServiceOrganization organization = ref == null ? null :
												   serviceOrganizationService.read(ref);
				obj.setServiceOrganization(organization);
			}
		});
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}
}
