package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.dao.StreetNameDao;
import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.StreetNameFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.esb.EsbSyncRequestExecutor;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.EsbXmlSyncObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.impl.NameTimeDependentServiceImpl;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.AttributeCopier;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.*;

@Transactional (readOnly = true)
public class StreetServiceImpl extends NameTimeDependentServiceImpl<
		StreetNameTranslation, StreetName, StreetNameTemporal, Street>
		implements StreetService, ParentService<StreetFilter> {

	private StreetDao streetDao;
	private StreetDaoExt streetDaoExt;
	private StreetNameDao streetNameDao;
	private TownDao townDao;

    private EsbSyncRequestExecutor<Street> esbSyncRequestExecutor;
	private DistrictService districtService;
	private ParentService<TownFilter> parentService;
	private SessionUtils sessionUtils;
	private ModificationListener<Street> modificationListener;

	/**
	 * Read name time-dependent object by its unique id
	 *
	 * @param streetStub Object stub
	 * @return object, or <code>null</code> if not found
	 */
	@Override
	public Street readFull(@NotNull Stub<Street> streetStub) {

		Street street = streetDao.readFull(streetStub.getId());
		if (street == null) {
			return null;
		}

		street.setTypeTemporals(treeSet(streetDao.findTypeTemporals(streetStub.getId())));

		return street;
	}

	/**
	 * Read streets collection by theirs ids
	 *
 	 * @param streetIds Street ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found streets
	 */
	@NotNull
	@Override
	public List<Street> readFull(@NotNull Collection<Long> streetIds, boolean preserveOrder) {

		List<Street> streets = streetDao.readFullCollection(streetIds, preserveOrder);
		Set<Street> orderedStreets = treeSet(streets, Street.<Street>comparator());

		// initialize types
		final int[] counter = { 0 };
		List<Street> streetTypes = streetDao.findWithTypes(streetIds);
		CollectionUtils.copyAttributes(orderedStreets, streetTypes, new AttributeCopier<Street>() {
			@Override
			public void copy(Street from, Street to) {
				log.debug("Copying type from street #{}, to street #{} ", from.getId(), to.getId());
                SortedSet<StreetTypeTemporal> types = from.getTypeTemporals();
				to.setTypeTemporals(types);
                if (types.isEmpty()) {
                    log.warn("Warning! Type for street with id {} is not available!!!!", from.getId());
                }
				++counter[0];
			}
		});

		if (log.isDebugEnabled()) {
			log.debug("Set type for {} streets of total {}", counter[0], streets.size());
			if (counter[0] != streets.size()) {
				log.error("Street ids: \n{}\nStreet with types ids: {}",
						DomainObject.collectionIds(orderedStreets),
						DomainObject.collectionIds(streetTypes));
			}
		}

		return streets;
	}

	/**
	 * Disable streets
	 *
	 * @param streetIds IDs of streets to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> streetIds) {
		for (Long id : streetIds) {
			if (id == null) {
				log.warn("Null id in collection of street ids for disable");
				continue;
			}
			Street street = streetDao.read(id);
			if (street == null) {
				log.warn("Can't get street with id {} from DB", id);
				continue;
			}
			street.disable();
			streetDao.update(street);

			modificationListener.onDelete(street);
			log.debug("Street disabled: {}", street);
		}

        Street street = new Street();
        street.setAction(EsbXmlSyncObject.ACTION_DELETE);
        street.setIds(streetIds);

        try {
            if (esbSyncRequestExecutor != null) {
                log.debug("Sending synchronizing request...");
                esbSyncRequestExecutor.executeRequest(street);
            }
        } catch (IOException e) {
            log.error("Error with synchronizing request");
        }

	}

	/**
	 * Create street
	 *
	 * @param street Street to save
	 * @return Saved instance of street
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Street create(@NotNull Street street) throws FlexPayExceptionContainer {

		validate(street);
		street.setId(null);
		streetDao.create(street);

		modificationListener.onCreate(street);

        street.setAction(EsbXmlSyncObject.ACTION_INSERT);

        try {
            if (esbSyncRequestExecutor != null) {
                log.debug("Sending synchronizing request...");
                esbSyncRequestExecutor.executeRequest(street);
            }
        } catch (IOException e) {
            log.error("Error with synchronizing request");
            throw new FlexPayExceptionContainer(new FlexPayException("Error with synchronizing request"));
        }

		return street;
	}

    @Transactional (readOnly = false)
    @NotNull
    private Street privateUpdate(@NotNull Street street) throws FlexPayExceptionContainer {

        validate(street);

        Street old = readFull(stub(street));
        if (old == null) {
            throw new FlexPayExceptionContainer(
                    new FlexPayException("No street found to update " + street));
        }
        sessionUtils.evict(old);
        modificationListener.onUpdate(old, street);

        streetDao.update(street);

        return street;
    }

	/**
	 * Update or create street
	 *
	 * @param street Street to save
	 * @return Saved instance of street
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Street update(@NotNull Street street) throws FlexPayExceptionContainer {

		privateUpdate(street);

        street.setAction(EsbXmlSyncObject.ACTION_UPDATE);

        try {
            if (esbSyncRequestExecutor != null) {
                log.debug("Sending synchronizing request...");
                esbSyncRequestExecutor.executeRequest(street);
            }
        } catch (IOException e) {
            log.error("Error with synchronizing request");
            throw new FlexPayExceptionContainer(new FlexPayException("Error with synchronizing request"));
        }

		return street;
	}

	/**
	 * Validate street before save
	 *
	 * @param street Street object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Street street) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (street.getParent() == null) {
			container.addException(new FlexPayException("No town", "ab.error.street.no_town"));
		}

		StreetNameTemporal nameTmprl = street.getCurrentNameTemporal();
		if (nameTmprl == null || nameTmprl.getValue() == null) {
			container.addException(new FlexPayException("No name", "ab.error.no_current_name"));
		} else {

			boolean defaultLangNameFound = false;

			for (StreetNameTranslation translation : nameTmprl.getValue().getTranslations()) {

				Language lang = translation.getLang();
				String name = translation.getName();
				boolean nameNotEmpty = StringUtils.isNotEmpty(name);

				if (lang.isDefault()) {
					defaultLangNameFound = nameNotEmpty;
				}

				if (nameNotEmpty) {
					List<Street> streets = streetDao.findByTownAndNameAndTypeAndLanguage(street.getParentStub().getId(),
							name, street.getCurrentType().getId(), lang.getId());
					if (!streets.isEmpty() && !streets.get(0).getId().equals(street.getId())) {
						container.addException(new FlexPayException(
								"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
					}
				}
			}

			if (!defaultLangNameFound) {
				container.addException(new FlexPayException(
						"No default language translation", "ab.error.street.full_name_is_required"));
			}
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Read street with its full hierarchical structure:
	 * country-region-town
	 *
	 * @param streetStub Street stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public Street readWithHierarchy(@NotNull Stub<Street> streetStub) {
		List<Street> streets = streetDao.findWithFullHierarchy(streetStub.getId());

		if (streets.isEmpty()) {
			return null;
		}

		Street street = streets.get(0);

		street.setTypeTemporals(treeSet(streetDao.findTypeTemporals(streetStub.getId())));

		return street;
	}

	/**
	 * Lookup streets by query and town id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * street_type street_name
	 *
	 * @param parentStub Town stub
	 * @param query searching string
	 * @return List of found streets
	 */
	@NotNull
	@Override
	public List<Street> findByParentAndQuery(@NotNull Stub<Town> parentStub, @NotNull String query) {
		return streetDao.findByParentAndQuery(parentStub.getId(), query);
	}

	/**
	 * Lookup streets by query and town id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * street_type street_name
	 *
	 * @param parentStub Town stub
	 * @param query searching string
	 * @param sorters sorters
	 * @param language language for search
	 * @param pager pager
	 * @return List of found streets
	 */
	@NotNull
	@Override
	public List<Street> findByParentAndQuery(@NotNull Stub<Town> parentStub, @NotNull List<? extends ObjectSorter> sorters,
										  @NotNull String query, @NotNull Language language, @NotNull Page<Street> pager) {
		Town town = townDao.read(parentStub.getId());
		if (town == null) {
			log.warn("Can't get town with id {} from DB", parentStub.getId());
			return list();
		}

		return streetDaoExt.findByParentAndQuery(parentStub.getId(), sorters, query, language.getId(), pager);
	}

	/**
	 * Lookup streets by name, street type and town
	 *
	 * @param townStub Town stub
	 * @param name Street name search string
	 * @param typeStub Street type stub
	 * @return List of found streets
	 */
	@NotNull
	@Override
	public List<Street> findByTownAndNameAndType(@NotNull Stub<Town> townStub, @NotNull String name, @NotNull Stub<StreetType> typeStub) {
		return streetDao.findByTownAndNameAndType(townStub.getId(), name.toUpperCase(), typeStub.getId());
	}

	/**
	 * Save street districts
	 *
	 * @param street	Street to save districts for
	 * @param districtIds List of district ids
	 * @return saved street object
	 * @throws FlexPayExceptionContainer if street validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Street saveDistricts(@NotNull Street street, @NotNull Set<Long> districtIds) throws FlexPayExceptionContainer {

		List<District> districts = districtService.readFull(districtIds, false);
		Set<StreetDistrictRelation> streetDistricts = set();
        for (District district : districts) {
            streetDistricts.add(new StreetDistrictRelation(street, district));
        }
        streetDaoExt.deleteStreetDistricts(street);
        street = readFull(Stub.stub(street));
        street.getStreetDistricts().addAll(streetDistricts);

        privateUpdate(street);

        street.setAction(EsbXmlSyncObject.ACTION_UPDATE_STREET_DISTRICTS);

        try {
            if (esbSyncRequestExecutor != null) {
                log.debug("Sending synchronizing request...");
                esbSyncRequestExecutor.executeRequest(street);
            }
        } catch (IOException e) {
            log.error("Error with synchronizing request");
            throw new FlexPayExceptionContainer(new FlexPayException("Error with synchronizing request"));
        }

		return street;
	}

	/**
	 * List all districts the street lays in
	 *
	 * @param streetStub Street stub
	 * @return List of districts
	 */
	@NotNull
	@Override
	public List<District> getStreetDistricts(@NotNull Stub<Street> streetStub) {
		return streetDao.findDistricts(streetStub.getId());
	}

	@NotNull
	@Override
	public String format(@NotNull Stub<Street> streetStub, @NotNull Locale locale, boolean shortMode) throws FlexPayException {
		Street street = streetDao.read(streetStub.getId());
		if (street == null) {
			throw new FlexPayException("Can't get street from DB", "common.error.invalid_id");
		}
		return street.format(locale, shortMode);
	}

	@NotNull
	@Override
	public List<Street> findSimpleByTown(Stub<Town> townStub, FetchRange range) {
		return streetDao.findSimpleByTown(townStub.getId(), range);
	}

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param forefatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public StreetFilter initFilter(@Nullable StreetFilter parentFilter, @NotNull PrimaryKeyFilter<?> forefatherFilter, @NotNull Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new StreetFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));

		Collection<StreetNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No street names", "ab.error.street.no_streets");
		}
		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			StreetName firstObject = (StreetName) names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(@NotNull StreetFilter filter) {
		if (!filter.needFilter()) {
			return true;
		}
		for (StreetNameTranslation nameTranslation : filter.getNames()) {
			StreetName name = (StreetName) nameTranslation.getTranslatable();
			if (name.getStub().getId().equals(filter.getSelectedId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Initialize filters
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters collection
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public ArrayStack initFilters(@Nullable ArrayStack filters, @NotNull Locale locale) throws FlexPayException {

		if (filters == null) {
			filters = new ArrayStack();
		}

		ObjectFilter filter = filters.isEmpty() ? null : (ObjectFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		TownFilter forefatherFilter = (TownFilter) filters.peek();

		if (filter instanceof StreetFilter) {
			StreetFilter parentFilter = (StreetFilter) filter;

			// init filter
			parentFilter = initFilter(parentFilter, forefatherFilter, locale);
			filters.push(parentFilter);
		} else if (filter instanceof StreetNameFilter) {
			StreetNameFilter streetNameFilter = (StreetNameFilter) filter;
			// check if selected street is in a town
			if (streetNameFilter.needFilter()) {
				Street selected = readFull(streetNameFilter.getSelectedStub());
				//noinspection ConstantConditions
				if (selected.getTownStub().equals(forefatherFilter.getSelectedStub())) {
					streetNameFilter.setSearchString(format(streetNameFilter.getSelectedStub(), locale, true));
				} else {
					streetNameFilter.setSearchString("");
					streetNameFilter.unsetSelected();
				}
			}
			filters.push(streetNameFilter);
		}

		return filters;
	}

    @Nullable
    @Override
    public Street findStreet(ArrayStack filters) {
        return streetDaoExt.findStreet(filters);
    }

    /**
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected StreetDao getNameTimeDependentDao() {
		return streetDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected StreetNameDao getNameValueDao() {
		return streetNameDao;
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        streetDao.setJpaTemplate(jpaTemplate);
        streetDaoExt.setJpaTemplate(jpaTemplate);
        streetNameDao.setJpaTemplate(jpaTemplate);
        townDao.setJpaTemplate(jpaTemplate);
        parentService.setJpaTemplate(jpaTemplate);
        districtService.setJpaTemplate(jpaTemplate);
        sessionUtils.setJpaTemplate(jpaTemplate);
        modificationListener.setJpaTemplate(jpaTemplate);
    }

    public void setEsbSyncRequestExecutor(EsbSyncRequestExecutor<Street> esbSyncRequestExecutor) {
        this.esbSyncRequestExecutor = esbSyncRequestExecutor;
    }

    @Required
	public void setStreetDao(StreetDao streetDao) {
		this.streetDao = streetDao;
	}

	@Required
	public void setStreetDaoExt(StreetDaoExt streetDaoExt) {
		this.streetDaoExt = streetDaoExt;
	}

	@Required
	public void setStreetNameDao(StreetNameDao streetNameDao) {
		this.streetNameDao = streetNameDao;
	}

	@Required
	public void setTownDao(TownDao townDao) {
		this.townDao = townDao;
	}

	@Required
	public void setParentService(ParentService<TownFilter> parentService) {
		this.parentService = parentService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<Street> modificationListener) {
		this.modificationListener = modificationListener;
	}

}
