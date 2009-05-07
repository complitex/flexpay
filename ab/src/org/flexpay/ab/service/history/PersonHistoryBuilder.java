package org.flexpay.ab.service.history;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryRecord;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.persistence.history.impl.HistoryBuilderBase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import static org.flexpay.common.util.CollectionUtils.list;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;
import static java.util.Collections.max;
import static java.util.Collections.min;

public class PersonHistoryBuilder extends HistoryBuilderBase<Person> {

	public static final int FIELD_IDENTITY = 1;

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

	private Logger log = LoggerFactory.getLogger(getClass());

	private IdentityTypeService identityTypeService;

	/**
	 * Build necessary diff records
	 *
	 * @param a1   First object
	 * @param a2   Second object
	 * @param diff Diff object
	 */
	protected void doDiff(@Nullable Person a1, @NotNull Person a2, @NotNull Diff diff) {

		log.debug("creating new persons diff");

		Person old = a1 == null ? new Person() : a2;

		buildIdentitiesDiff(old, a2, diff);
	}

	private void buildIdentitiesDiff(@NotNull Person a1, @NotNull Person a2, @NotNull Diff diff) {

		// split both person identities by types
		SortedMap<IdentityType, List<PersonIdentity>> idents1 = prepareIdentities(a1);
		SortedMap<IdentityType, List<PersonIdentity>> idents2 = prepareIdentities(a2);

		// build set of all unique types
		Set<IdentityType> types = CollectionUtils.set(idents1.keySet());
		types.addAll(idents2.keySet());
		log.debug("Unique types count: {}", types.size());

		// now for every unique type build separate diff records
		for (IdentityType type : types) {

			List<PersonIdentity> identsList1 = idents1.get(type);
			identsList1 = identsList1 != null ? identsList1 : Collections.<PersonIdentity>emptyList();

			List<PersonIdentity> identsList2 = idents2.get(type);
			identsList2 = identsList2 != null ? identsList2 : Collections.<PersonIdentity>emptyList();

			buildTypeDiff(identsList1, identsList2, diff);
		}
	}

	private void buildTypeDiff(List<PersonIdentity> idents1, List<PersonIdentity> idents2, Diff diff) {

		Iterator<PersonIdentity> it1 = idents1.iterator();
		Iterator<PersonIdentity> it2 = idents2.iterator();

		Date cursor = ApplicationConfig.getPastInfinite();
		PersonIdentity n1 = null;
		PersonIdentity n2 = null;

		while (it1.hasNext() || it2.hasNext()) {
			n1 = n1 == null && it1.hasNext() ? it1.next() : n1;
			n2 = n2 == null && it2.hasNext() ? it2.next() : n2;

			// setup next intervals boundaries
			Date begin1 = n1 != null ? n1.getBeginDate() : ApplicationConfig.getFutureInfinite();
			Date begin2 = n2 != null ? n2.getBeginDate() : ApplicationConfig.getFutureInfinite();
			Date end1 = n1 != null ? n1.getEndDate() : ApplicationConfig.getFutureInfinite();
			Date end2 = n2 != null ? n2.getEndDate() : ApplicationConfig.getFutureInfinite();

			// setup lower and upper bound for a next pair of intervals to build diffs on
			Date beginMin = min(list(begin1, begin2));
			Date beginMax = max(list(begin1, begin2));
			Date end = min(list(end1, end2));
			Date beginLower = min(list(beginMax, end));

			// add diff in interval from cursor to min among begins
			addIdentityDiff(cursor, beginMin, n1, n2, diff);

			// set cursor to next point - first begin, or cursor if it was after begin
			cursor = max(list(beginMin, cursor));
			// now add diff from cursor to lower value of bigger begin and two ends
			addIdentityDiff(cursor, beginLower, n1, n2, diff);

			cursor = beginLower;
			// if not reached any of ends, add diff from max begin to min end
			if (cursor.before(end)) {
				addIdentityDiff(cursor, end, n1, n2, diff);
				cursor = end;
			}

			// if the first end was reached - fetch next value
			if (cursor.compareTo(end1) >= 0) {
				n1 = null;
			}
			// if the second end was reached - fetch next value
			if (cursor.compareTo(end2) >= 0) {
				n2 = null;
			}
		}
	}

	private void addIdentityDiff(Date begin, Date end, PersonIdentity n1, PersonIdentity n2, Diff diff) {

		if (log.isDebugEnabled()) {
			log.debug("Adding identity diff in interval: {} - {}", DateUtil.format(begin), DateUtil.format(end));
		}

		if (begin.compareTo(ApplicationConfig.getFutureInfinite()) >= 0) {
			return;
		}
		if (end.compareTo(ApplicationConfig.getPastInfinite()) <= 0) {
			return;
		}
		if (n1 != null && n1.getBeginDate().compareTo(end) >= 0) {
			n1 = null;
		}
		if (n2 != null && n2.getBeginDate().compareTo(end) >= 0) {
			n2 = null;
		}
		if (begin.after(end) || (n1 == null && n2 == null)) {
			return;
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
		rec.setFieldKey2(n1 == null || n1.getId() == null ? null : String.valueOf(n1.getId()));
		rec.setFieldKey3(n2 == null || n2.getId() == null ? null : String.valueOf(n2.getId()));
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
		SortedMap<IdentityType, List<PersonIdentity>> result = CollectionUtils.treeMap();

		// split identities by type
		for (PersonIdentity identity : identities) {
			IdentityType type = identity.getIdentityType();
			List<PersonIdentity> idents = result.get(type);
			if (idents == null) {
				idents = CollectionUtils.list();
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

	public void patch(@NotNull Person person, @NotNull Diff diff) {

		IdentityPatchContext context = new IdentityPatchContext();
		for (HistoryRecord record : diff.getHistoryRecords()) {
			log.debug("Person patch record: {}", record);

			switch (record.getFieldType()) {
				case FIELD_IDENTITY:
					patchIdentity(context, person, record);
					break;
				default:
					log.warn("Unsupported field type {}", record);
					record.setProcessingStatus(ProcessingStatus.STATUS_IGNORED);
			}
		}

		if (context.lastIdentity != null) {
			person.setIdentity(context.lastIdentity);
		}
	}

	private static class IdentityPatchContext {
		PersonIdentity lastIdentity;
		String oldTypeId;
		String newTypeId;
	}

	private void patchIdentity(IdentityPatchContext context, Person person, HistoryRecord record) {

		if (!equals(record.getFieldKey2(),context.oldTypeId) || !equals(record.getFieldKey3(), context.newTypeId)) {
			if (context.lastIdentity != null) {
				person.setIdentity(context.lastIdentity);
			}

			context.lastIdentity = new PersonIdentity();
			context.lastIdentity.setBeginDate(record.getBeginDate());
			context.lastIdentity.setEndDate(record.getEndDate());
			context.oldTypeId = record.getFieldKey2();
			context.newTypeId = record.getFieldKey3();
		}

		if (KEY_IDENTITY_ID.equals(record.getFieldKey())) {
			String externalId = record.getNewStringValue();
			Stub<IdentityType> stub = correctionsService.findCorrection(
					externalId, IdentityType.class, masterIndexService.getMasterSourceDescription());
			if (stub == null) {
				throw new IllegalStateException("Cannot find identity type by master index: " + externalId);
			}
			IdentityType type = identityTypeService.read(stub.getId());
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
			context.lastIdentity.setSex(val == null ? PersonIdentity.SEX_UNKNOWN : (short)val.intValue());
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
}
