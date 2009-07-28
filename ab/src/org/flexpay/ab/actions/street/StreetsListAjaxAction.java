package org.flexpay.ab.actions.street;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.sorter.StreetSorterByName;
import org.flexpay.ab.persistence.sorter.StreetSorterByType;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import org.flexpay.common.util.LanguageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StreetsListAjaxAction extends FPActionWithPagerSupport<Street> {

	private String streetId;
	private String townId;
	private List<Street> streets = list();

	private StreetSorterByName streetSorterByName = new StreetSorterByName();
	private StreetSorterByType streetSorterByType = new StreetSorterByType();

	private StreetService streetService;

	@Override
	@NotNull
	public String doExecute() throws Exception {

		Long streetIdLong = null;
		if (StringUtils.isNotBlank(streetId)) {
			try {
				streetIdLong = Long.parseLong(streetId);
			} catch (Exception e) {
				log.warn("Incorrect street id in filter ({})", streetId);
			}
		}

		if (streetIdLong != null) {
			streets = new ArrayList<Street>();
			streets.add(streetService.readFull(new Stub<Street>(streetIdLong)));
			return SUCCESS;
		}

		Locale locale = getUserPreferences().getLocale();

		streetSorterByName.setLang(LanguageUtil.getLanguage(locale));
		streetSorterByType.setLang(LanguageUtil.getLanguage(locale));

		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(streetSorterByName, streetSorterByType);

		Long townIdLong;

		try {
			townIdLong = Long.parseLong(townId);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", townId);
			return SUCCESS;
		}

		List<Street> streetsStubs = streetService.getStreets(new Stub<Town>(townIdLong), sorters, getPager());
		log.info("Total streets found: {}", streetsStubs);
		streets = new ArrayList<Street>();

		for (Street street : streetsStubs) {
			streets.add(streetService.readFull(stub(street)));
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@Override
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setTownId(String townId) {
		this.townId = townId;
	}

	public void setStreetId(String streetId) {
		this.streetId = streetId;
	}

	public List<Street> getStreets() {
		return streets;
	}

	public StreetSorterByName getStreetSorterByName() {
		return streetSorterByName;
	}

	public void setStreetSorterByName(StreetSorterByName streetSorterByName) {
		this.streetSorterByName = streetSorterByName;
	}

	public StreetSorterByType getStreetSorterByType() {
		return streetSorterByType;
	}

	public void setStreetSorterByType(StreetSorterByType streetSorterByType) {
		this.streetSorterByType = streetSorterByType;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}