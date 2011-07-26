package org.flexpay.payments.service.history;

import org.apache.commons.lang.ObjectUtils;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.builder.ReferenceExtractor;
import org.flexpay.common.persistence.history.builder.ReferencePatcher;
import org.flexpay.common.persistence.history.builder.TranslationExtractor;
import org.flexpay.common.persistence.history.builder.TranslationPatcher;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.EqualsHelper;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceDescription;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ServiceHistoryBuilder extends HistoryBuilderBase<Service> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	public static final int FIELD_PROVIDER_ID = 1;
	public static final int FIELD_DESCRIPTION = 2;
	public static final int FIELD_SERVICETYPE_ID = 3;
	public static final int FIELD_EXTERNAL_CODE = 4;
	public static final int FIELD_BEGIN_DATE = 5;
	public static final int FIELD_END_DATE = 6;
	public static final int FIELD_MEASURE_UNIT_ID = 7;
	public static final int FIELD_PARENT_SERVICE_ID = 8;

	private ServiceProviderService serviceProviderService;
	private SPService spService;

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
    @Override
	protected void doDiff(@Nullable Service t1, @NotNull Service t2, @NotNull Diff diff) {

		log.debug("creating new services diff");
		if (!t2.isActive()) {
			diff.setOperationType(HistoryOperationType.TYPE_DELETE);
		}

		Service old = t1 == null ? new Service() : t1;

		buildSimpleFieldsDiff(old, t2, diff);
		buildDescriptionDiff(old, t2, diff);
		buildProviderRefDiff(old, t2, diff);
		buildMeasureUnitRefDiff(old, t2, diff);
		buildServiceTypeRefDiff(old, t2, diff);
		buildParentServiceRefDiff(old, t2, diff);
	}

	private void buildSimpleFieldsDiff(Service p1, Service p2, Diff diff) {

		if (!EqualsHelper.strEquals(p1.getExternalCode(), p2.getExternalCode())) {
			HistoryRecord record = new HistoryRecord();
			record.setOldStringValue(p1.getExternalCode());
			record.setNewStringValue(p2.getExternalCode());
			record.setFieldType(FIELD_EXTERNAL_CODE);
			diff.addRecord(record);
			log.debug("Added external code diff record: {}", record);
		}

		if (!ObjectUtils.equals(p1.getBeginDate(), p2.getBeginDate())) {
			HistoryRecord record = new HistoryRecord();
			record.setOldDateValue(p1.getBeginDate());
			record.setNewDateValue(p2.getBeginDate());
			record.setFieldType(FIELD_BEGIN_DATE);
			diff.addRecord(record);
			log.debug("Added begin date diff record: {}", record);
		}

		if (!ObjectUtils.equals(p1.getEndDate(), p2.getEndDate())) {
			HistoryRecord record = new HistoryRecord();
			record.setOldDateValue(p1.getEndDate());
			record.setNewDateValue(p2.getEndDate());
			record.setFieldType(FIELD_END_DATE);
			diff.addRecord(record);
			log.debug("Added end date diff record: {}", record);
		}
	}

	private void buildDescriptionDiff(Service p1, Service p2, Diff diff) {

		builderHelper.buildTranslationDiff(p1, p2, diff, new TranslationExtractor<Translation, Service>() {
            @Override
			public Translation getTranslation(Service obj, @NotNull Language language) {
				return obj.getDescription(language);
			}

            @Override
			public int getTranslationField() {
				return FIELD_DESCRIPTION;
			}
		});
	}

	private void buildProviderRefDiff(Service p1, Service p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<ServiceProvider, Service>() {
            @Override
			public ServiceProvider getReference(Service obj) {
				return obj.getServiceProvider();
			}

            @Override
			public int getReferenceField() {
				return FIELD_PROVIDER_ID;
			}
		});
	}

	private void buildMeasureUnitRefDiff(Service p1, Service p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<MeasureUnit, Service>() {
            @Override
			public MeasureUnit getReference(Service obj) {
				return obj.getMeasureUnit();
			}

            @Override
			public int getReferenceField() {
				return FIELD_MEASURE_UNIT_ID;
			}
		});
	}

	private void buildParentServiceRefDiff(Service p1, Service p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<Service, Service>() {
            @Override
			public Service getReference(Service obj) {
				return obj.getParentService();
			}

            @Override
			public int getReferenceField() {
				return FIELD_PARENT_SERVICE_ID;
			}
		});
	}

	private void buildServiceTypeRefDiff(Service p1, Service p2, Diff diff) {

		builderHelper.buildReferenceDiff(p1, p2, diff, new ReferenceExtractor<ServiceType, Service>() {
            @Override
			public ServiceType getReference(Service obj) {
				return obj.getServiceType();
			}

            @Override
			public int getReferenceField() {
				return FIELD_SERVICETYPE_ID;
			}
		});
	}

	/**
	 * Apply diff to an object
	 *
	 * @param service Object to apply diff to
	 * @param diff	Diff to apply
	 */
    @Override
	public void patch(@NotNull Service service, @NotNull Diff diff) {

		for (HistoryRecord record : diff.getHistoryRecords()) {
			switch (record.getFieldType()) {
				case FIELD_DESCRIPTION:
					patchDescription(service, record);
					break;
				case FIELD_BEGIN_DATE:
					log.debug("Patching service begin date {}", record);
					service.setBeginDate(record.getNewDateValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_END_DATE:
					log.debug("Patching service end date {}", record);
					service.setEndDate(record.getNewDateValue());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_EXTERNAL_CODE:
					log.debug("Patching service external code {}", record);
					service.setExternalCode(record.getNewStringValueNotNull());
					record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
					break;
				case FIELD_SERVICETYPE_ID:
					patchServiceTypeReference(service, record);
					break;
				case FIELD_MEASURE_UNIT_ID:
					patchMeasureUnitReference(service, record);
					break;
				case FIELD_PARENT_SERVICE_ID:
					patchParentServiceReference(service, record);
					break;
				case FIELD_PROVIDER_ID:
					patchProviderReference(service, record);
					break;
				default:
					log.info("Unsupported record: {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}

		log.debug("End patch");
	}

	private void patchDescription(Service service, HistoryRecord record) {

		log.debug("Patching service description {}", record);
		builderHelper.patchTranslation(service, record, new TranslationPatcher<ServiceDescription, Service>() {
            @Override
			public ServiceDescription getNotNullTranslation(Service obj, @NotNull Language language) {
				ServiceDescription tr = obj.getDescription(language);
				return tr == null ? new ServiceDescription() : tr;
			}

            @Override
			public void setTranslation(Service obj, ServiceDescription tr, String name) {
				tr.setName(name);
				obj.setDescription(tr);
			}
		});
	}

	private void patchProviderReference(@NotNull Service service, @NotNull HistoryRecord record) {
		log.debug("Patching service provider reference {}", record);

		builderHelper.patchReference(service, record, new ReferencePatcher<ServiceProvider, Service>() {
            @Override
			public Class<ServiceProvider> getType() {
				return ServiceProvider.class;
			}

            @Override
			public void setReference(Service obj, Stub<ServiceProvider> ref) {
				ServiceProvider provider = serviceProviderService.read(ref);
				obj.setServiceProvider(provider);
			}
		});
	}

	private void patchServiceTypeReference(@NotNull Service service, @NotNull HistoryRecord record) {
		log.debug("Patching service type reference {}", record);

		builderHelper.patchReference(service, record, new ReferencePatcher<ServiceType, Service>() {
            @Override
			public Class<ServiceType> getType() {
				return ServiceType.class;
			}

            @Override
			public void setReference(Service obj, Stub<ServiceType> ref) {
				obj.setServiceType(new ServiceType(ref));
			}
		});
	}

	private void patchMeasureUnitReference(@NotNull Service service, @NotNull HistoryRecord record) {
		log.debug("Patching measure unit reference {}", record);

		builderHelper.patchReference(service, record, new ReferencePatcher<MeasureUnit, Service>() {
            @Override
			public Class<MeasureUnit> getType() {
				return MeasureUnit.class;
			}

            @Override
			public void setReference(Service obj, Stub<MeasureUnit> ref) {
				if (ref == null) {
					obj.setMeasureUnit(null);
				} else {
					obj.setMeasureUnit(new MeasureUnit(ref));
				}
			}
		});
	}

	private void patchParentServiceReference(@NotNull Service service, @NotNull HistoryRecord record) {
		log.debug("Patching parent service reference {}", record);

		builderHelper.patchReference(service, record, new ReferencePatcher<Service, Service>() {
            @Override
			public Class<Service> getType() {
				return Service.class;
			}

            @Override
			public void setReference(Service obj, Stub<Service> ref) {
				if (ref == null) {
					obj.setParentService(null);
				} else {
					Service parent = spService.readFull(ref);
					if (parent == null) {
						throw new IllegalStateException("Expected parent service but not found: " + ref);
					}
					obj.setParentService(parent);
				}
			}
		});
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}
