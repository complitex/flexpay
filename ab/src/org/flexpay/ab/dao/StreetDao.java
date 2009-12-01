package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetTypeTemporal;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.dao.paging.FetchRange;

import java.util.Collection;
import java.util.List;

public interface StreetDao extends NameTimeDependentDao<Street, Long> {

	List<Street> findByParentAndQuery(Long townId, String query);

	List<Street> findByTownAndNameAndTypeAndLanguage(Long townId, String name, Long typeId, Long languageId);

	List<Street> findByTownAndNameAndType(Long townId, String name, Long typeId);

	List<Street> findWithFullHierarchy(Long streetId);

	List<StreetTypeTemporal> findTypeTemporals(Long streetId);

	List<District> findDistricts(Long streetId);

	List<Street> findSimpleByTown(Long townId, FetchRange range);

	List<Street> findWithTypes(Collection<Long> streetIds);
}
