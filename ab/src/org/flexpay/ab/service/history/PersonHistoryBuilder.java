package org.flexpay.ab.service.history;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.TemporalObjectsHistoryBuildHelper;
import org.flexpay.common.persistence.history.TemporalObjectsHistoryBuildHelper.TemporalDataExtractor;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

import static org.flexpay.common.util.CollectionUtils.*;

public class PersonHistoryBuilder extends HistoryBuilderBase<Person> {

	public static final int FIELD_IDENTITY = 1;
	public static final int FIELD_REGISTRATION = 2;

	public static final String KEY_IDENTITY_ID = "IDENTITY_ID";
	public static final String KEY_BIRTH_DATE = "BIRTH_DATE";
	public static final String KEY_ORGANIZATION = "ORGANIZATION";
	public static final String KEY_FIRST_NAME = "FIRST_NAME";
	public static final String KEY_MIDDLE_NAME = "MIDDLE_NAME";
	public static final String KEY_LAST_NAME = "LAST_NAME";
	public static final String KEY_SERIAL_NUMBER = "SERIAL_NUMBER";
	public static final String KEY_DOCUMENT_NUMBER = "DOCUMENT_NUMBER";
	public static final String KEY_SEX = "SEX";
	public static final String KEY_DEFAULT_FLAG = "DEFAULT_FLAG";
	public static final String KEY_APARTMENT_ID = "APARTMENT_ID";

	private Logger log = LoggerFactory.getLogger(getClass());

	private ApartmentService apartmentService;
	private IdentityTypeService identityTypeService;

	/**
	 * Build necessary diff records
	 *
	 * @param a1   First object
	 * @param a2   Second object
	 * @param diff Diff object
	 */
	@Override
	protected void doDiff(@Nullable Person a1, @NotNull Person a2, @NotNull Diff diff) {

		log.debug("creating new persons diff");

		Person old = a1 == null ? new Person() : a1;

		buildIdentitiesDiff(old, a2, diff);
		buildRegistrationDiff(old, a2, diff);
	}

	private void buildRegistrationDiff(@NotNull Person a1, @NotNull Person a2, @NotNull Diff diff) {

		List<PersonRegistration> regs1 = list(a1.getPersonRegistrations());
		Collections.sort(regs1);
		List<PersonRegistration> regs2 = list(a2.getPersonRegistrations());
		Collections.sort(regs2);

		TemporalObjectsHistoryBuildHelper.buildDiff(new TemporalDataExtractor<PersonRegistration>() {

			@Override
			public Date getBeginDate(PersonRegistration obj) {
				return obj.getBeginDate();
			}

			@Override
			public Date getEndDate(PersonRegistration obj) {
				return obj.getEndDate();
			}

			@Override
			public void buildDiff(Date begin, Date end, PersonRegistration t1, PersonRegistration t2, Diff df) {
				addRegistrationDiff(begin, end, t1, t2, df);
			}
		}, regs1, regs2, diff);
	}

	private void addRegistrationDiff(Date begin, Date end, PersonRegistration n1, PersonRegistration n2, Diff diff) {

		if (log.isDebugEnabled()) {
			log.debug("Adding registration diff in interval: {} - {}", DateUtil.format(begin), DateUtil.format(end));
		}

		Apartment a1 = n1 == null ? null : n1.getApartment();
		Apartment a2 = n2 == null ? null : n2.getApartment();
		if (!equals(a1, a2)) {
			HistoryRecord rec = new HistoryRecord();
			rec.setFieldType(FIELD_REGISTRATION);
			rec.setFieldKey(KEY_APARTMENT_ID);
			rec.setOldStringValue(a1 == null ? null : masterIndexService.getMasterIndex(a1));
			rec.setNewStringValue(a2 == null ? null : masterIndexService.getMasterIndex(a2));
			rec.setBeginDate(begin);
			rec.setEndDate(end);
			diff.addRecord(rec);

			log.debug("Added registration record {}", rec);
		}
	}

	private void buildIdentitiesDiff(@NotNull Person a1, @NotNull Person a2, @NotNull Diff diff) {

		// split both person identities by types
		SortedMap<IdentityType, List<PersonIdentity>> idents1 = prepareIdentities(a1);
		SortedMap<IdentityType, List<PersonIdentity>> idents2 = prepareIdentities(a2);

		// build set of all unique types
		Set<IdentityType> types = set(idents1.keySet());
		types.addAll(idents2.keySet());
		log.debug("Unique types count: {}", types.size());

		// now for every unique type build separate diff records
		for (IdentityType type : types) {

			List<PersonIdentity> identsList1 = idents1.get(type);
			identsList1 = identsList1 != null ? identsList1 : Collections.<PersonIdentity>emptyList();

			List<PersonIdentity> identsList2 = idents2.get(type);
			identsList2 = identsList2 != null ? identsList2 : Collections.<PersonIdentity>emptyList();

			log.debug("Type diff: {}", type);
			buildTypeDiff(identsList1, identsList2, diff);
		}
	}

	private void buildTypeDiff(List<PersonIdentity> idents1, List<PersonIdentity> idents2, Diff diff) {

		TemporalObjectsHistoryBuildHelper.buildDiff(new TemporalDataExtractor<PersonIdentity>() {
			@Override
			public Date getBeginDate(PersonIdentity obj) {
				return obj.getBeginDate();
			}

			@Override
			public Date getEndDate(PersonIdentity obj) {
				return obj.getEndDate();
			}

			@Override
			public void buildDiff(Date begin, Date end, PersonIdentity t1, PersonIdentity t2, Diff df) {
				addIdentityDiff(begin, end, t1, t2, df);
			}
		}, idents1, idents2, diff);
	}

	private void addIdentityDiff(Date begin, Date end, PersonIdentity n1, PersonIdentity n2, Diff diff) {

		if (log.isDebugEnabled()) {
			log.debug("Adding identity diff in interval: {} - {}", DateUtil.format(begin), DateUtil.format(end));
		}

		IdentityType type1 = n1 == null ? null : n1.getIdentityType();
		IdentityType type2 = n2 == null ? null : n2.getIdentityType();
		if (!equals(type1, type2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_IDENTITY_ID);
			rec.setOldStringValue(type1 == null ? null : masterIndexService.getMasterIndex(type1));
			rec.setNewStringValue(type2 == null ? null : masterIndexService.getMasterIndex(type2));
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		Date birthDate1 = n1 == null ? null : n1.getBirthDate();
		Date birthDate2 = n2 == null ? null : n2.getBirthDate();
		if (!equals(birthDate1, birthDate2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_BIRTH_DATE);
			rec.setOldDateValue(birthDate1);
			rec.setNewDateValue(birthDate2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		String org1 = n1 == null ? null : n1.getOrganization();
		String org2 = n2 == null ? null : n2.getOrganization();
		if (!equals(org1, org2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_ORGANIZATION);
			rec.setOldStringValue(org1);
			rec.setNewStringValue(org2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		String fn1 = n1 == null ? null : n1.getFirstName();
		String fn2 = n2 == null ? null : n2.getFirstName();
		if (!equals(fn1, fn2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_FIRST_NAME);
			rec.setOldStringValue(fn1);
			rec.setNewStringValue(fn2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		String mn1 = n1 == null ? null : n1.getMiddleName();
		String mn2 = n2 == null ? null : n2.getMiddleName();
		if (!equals(mn1, mn2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_MIDDLE_NAME);
			rec.setOldStringValue(mn1);
			rec.setNewStringValue(mn2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		String ln1 = n1 == null ? null : n1.getLastName();
		String ln2 = n2 == null ? null : n2.getLastName();
		if (!equals(ln1, ln2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_LAST_NAME);
			rec.setOldStringValue(ln1);
			rec.setNewStringValue(ln2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		String sn1 = n1 == null ? null : n1.getSerialNumber();
		String sn2 = n2 == null ? null : n2.getSerialNumber();
		if (!equals(sn1, sn2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_SERIAL_NUMBER);
			rec.setOldStringValue(sn1);
			rec.setNewStringValue(sn2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		String dn1 = n1 == null ? null : n1.getDocumentNumber();
		String dn2 = n2 == null ? null : n2.getDocumentNumber();
		if (!equals(dn1, dn2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_DOCUMENT_NUMBER);
			rec.setOldStringValue(dn1);
			rec.setNewStringValue(dn2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		Integer s1 = n1 == null ? null : (int) n1.getSex();
		Integer s2 = n2 == null ? null : (int) n2.getSex();
		if (!equals(s1, s2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_SEX);
			rec.setOldIntValue(s1);
			rec.setNewIntValue(s2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}

		Boolean d1 = n1 == null ? null : n1.isDefault();
		Boolean d2 = n2 == null ? null : n2.isDefault();
		if (!equals(d1, d2)) {
			HistoryRecord rec = newIdentityRecord(begin, end, n1, n2);
			rec.setFieldKey(KEY_DEFAULT_FLAG);
			rec.setOldBoolValue(d1);
			rec.setNewBoolValue(d2);
			diff.addRecord(rec);
			log.debug("Added record {}", rec);
		}
	}

	private boolean equals(String s1, String s2) {
		return StringUtils.trimToEmpty(s1).equals(StringUtils.trimToEmpty(s2));
	}

	private boolean equals(Object o1, Object o2) {
		return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
	}

	private HistoryRecord newIdentityRecord(Date begin, Date end, PersonIdentity n1, PersonIdentity n2) {

		HistoryRecord rec = new HistoryRecord();
		rec.setFieldType(FIELD_IDENTITY);
		IdentityType type = n1 != null ? n1.getIdentityType() :
							n2 != null ? n2.getIdentityType() : null;
		String key2 = (n1 == null || n1.getId() == null ? null : String.valueOf(n1.getId())) + "|" +
					(n2 == null || n2.getId() == null ? null : String.valueOf(n2.getId()));
		rec.setFieldKey2(key2);
		rec.setFieldKey3(type != null ? masterIndexService.getMasterIndex(type) : null);
		rec.setBeginDate(begin);
		rec.setEndDate(end);
		return rec;
	}

	/**
	 * split person identities by types
	 *
	 * @param person Person to split identities for
	 * @return splitted identities
	 */
	private SortedMap<IdentityType, List<PersonIdentity>> prepareIdentities(Person person) {

		Set<PersonIdentity> identities = person.getPersonIdentities();
		SortedMap<IdentityType, List<PersonIdentity>> result = treeMap();

		// split identities by type
		for (PersonIdentity identity : identities) {
			IdentityType type = identity.getIdentityType();
			List<PersonIdentity> idents = result.get(type);
			if (idents == null) {
				idents = list();
				result.put(type, idents);
			}
			idents.add(identity);
		}

		// now sort splitted identities by document begin date
		for (List<PersonIdentity> idents : result.values()) {
			Collections.sort(idents);
		}
		return result;
	}

	@Override
	public void patch(@NotNull Person person, @NotNull Diff diff) {

		IdentityPatchContext context = new IdentityPatchContext();
		for (HistoryRecord record : diff.getHistoryRecords()) {
			log.debug("Person patch record: {}", record);

			switch (record.getFieldType()) {
				case FIELD_IDENTITY:
					patchIdentity(context, person, record);
					break;
				case FIELD_REGISTRATION:
					patchRegistration(person, record);
					break;
				default:
					log.warn("Unsupported field type {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}

		if (context.lastIdentity != null) {
			updateIdentityType(context);
			person.setIdentity(context.lastIdentity);
		}
	}

	private void patchRegistration(Person person, HistoryRecord record) {

		if (KEY_APARTMENT_ID.equals(record.getFieldKey())) {
			String externalId = record.getNewStringValue();
			Stub<Apartment> stub = correctionsService.findCorrection(
					externalId, Apartment.class, masterIndexService.getMasterSourceDescriptionStub());
			Apartment apartment = stub == null ? null : apartmentService.readFull(stub);
			person.setPersonRegistration(apartment, record.getBeginDate(), record.getEndDate());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			log.debug("Set person registration");
		}
	}

	private static class IdentityPatchContext {
		PersonIdentity lastIdentity;
		String prevIdentityId;
		String prevTypeMasterIndex;
	}

	/**
	 * Unless identity type is not a implicit set it via fieldKey3 property each Identity diff record has
	 *
	 * @param context Identity patch context
	 */
	private void updateIdentityType(IdentityPatchContext context) {
		PersonIdentity identity = context.lastIdentity;
		if (identity != null && identity.getIdentityType() == null && context.prevTypeMasterIndex != null) {
			Stub<IdentityType> stub = correctionsService.findCorrection(
					context.prevTypeMasterIndex, IdentityType.class, masterIndexService.getMasterSourceDescriptionStub());
			if (stub == null) {
				throw new IllegalStateException("Cannot find identity type by master index: " + context.prevTypeMasterIndex);
			}
			IdentityType type = identityTypeService.readFull(stub);
			context.lastIdentity.setIdentityType(type);
		}
	}

	private void patchIdentity(IdentityPatchContext context, Person person, HistoryRecord record) {

		if (!equals(record.getFieldKey2(), context.prevIdentityId)) {
			if (context.lastIdentity != null) {
				updateIdentityType(context);
				person.setIdentity(context.lastIdentity);
			}

			context.lastIdentity = new PersonIdentity();
			context.lastIdentity.setFirstName("");
			context.lastIdentity.setMiddleName("");
			context.lastIdentity.setLastName("");
			context.lastIdentity.setOrganization("");
			context.lastIdentity.setDocumentNumber("");
			context.lastIdentity.setSerialNumber("");
			context.lastIdentity.setBirthDate(ApplicationConfig.getPastInfinite());
			context.lastIdentity.setDefault(false);
			context.lastIdentity.setSex(PersonIdentity.SEX_UNKNOWN);

			context.lastIdentity.setBeginDate(record.getBeginDate());
			context.lastIdentity.setEndDate(record.getEndDate());
			context.prevIdentityId = record.getFieldKey2();
			context.prevTypeMasterIndex = record.getFieldKey3();
		}

		if (KEY_IDENTITY_ID.equals(record.getFieldKey())) {
			String externalId = record.getNewStringValue();
			Stub<IdentityType> stub = correctionsService.findCorrection(
					externalId, IdentityType.class, masterIndexService.getMasterSourceDescriptionStub());
			if (stub == null) {
				throw new IllegalStateException("Cannot find identity type by master index: " + externalId);
			}
			IdentityType type = identityTypeService.readFull(stub);
			context.lastIdentity.setIdentityType(type);
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			log.debug("Set identity type: {}", type);
			return;
		}

		if (KEY_BIRTH_DATE.equals(record.getFieldKey())) {
			context.lastIdentity.setBirthDate(record.getNewDateValue());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		if (KEY_ORGANIZATION.equals(record.getFieldKey())) {
			context.lastIdentity.setOrganization(record.getNewStringValue());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		if (KEY_FIRST_NAME.equals(record.getFieldKey())) {
			context.lastIdentity.setFirstName(record.getNewStringValue());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		if (KEY_MIDDLE_NAME.equals(record.getFieldKey())) {
			context.lastIdentity.setMiddleName(record.getNewStringValue());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		if (KEY_LAST_NAME.equals(record.getFieldKey())) {
			context.lastIdentity.setLastName(record.getNewStringValue());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		if (KEY_SERIAL_NUMBER.equals(record.getFieldKey())) {
			context.lastIdentity.setSerialNumber(record.getNewStringValue());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		if (KEY_DOCUMENT_NUMBER.equals(record.getFieldKey())) {
			context.lastIdentity.setDocumentNumber(record.getNewStringValue());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		if (KEY_SEX.equals(record.getFieldKey())) {
			Integer val = record.getNewIntValue();
			context.lastIdentity.setSex(val == null ? PersonIdentity.SEX_UNKNOWN : (short) val.intValue());
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		if (KEY_DEFAULT_FLAG.equals(record.getFieldKey())) {
			Boolean val = record.getNewBoolValue();
			context.lastIdentity.setDefault(val == null ? false : val);
			record.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			return;
		}

		log.warn("Unknown field key found: {}", record.getFieldKey());
		record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
	}

	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}
}
