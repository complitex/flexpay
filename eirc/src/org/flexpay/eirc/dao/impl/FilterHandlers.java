package org.flexpay.eirc.dao.impl;

import org.flexpay.ab.persistence.filters.*;
import org.flexpay.common.dao.FilterHandler;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public final class FilterHandlers {

	private FilterHandlers() {
	}

	@NotNull
	public static List<FilterHandler> eircAccountFilterHandlers() {
		return list(
				new ApartmentFilterHandler(),
				new BuildingsFilterHandler(),
                new StreetFilterHandler(),
                new TownFilterHandler(),
                new PersonSearchFilterHandler()
		);
	}

	public static final class ApartmentFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof ApartmentFilter;
		}

		@Override
		public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
			clause.append("apartment.id=?");
			ApartmentFilter f = (ApartmentFilter) filter;
			return list(f.getSelectedStub().getId());
		}
	}

	public static final class BuildingsFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof BuildingsFilter;
		}

		@Override
		public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
			clause.append("buildingses.id=?");
			BuildingsFilter f = (BuildingsFilter) filter;
			return list(f.getSelectedStub().getId());
		}
	}

    public static final class StreetFilterHandler implements FilterHandler {

        @Override
        public boolean supports(ObjectFilter filter) {
            return filter instanceof StreetFilter;
        }

        @Override
        public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
            clause.append("street.id=?");
            StreetFilter f = (StreetFilter) filter;
            return list(f.getSelectedStub().getId());
        }
    }

    public static final class TownFilterHandler implements FilterHandler {

        @Override
        public boolean supports(ObjectFilter filter) {
            return filter instanceof TownFilter;
        }

        @Override
        public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
            clause.append("town.id=?");
            TownFilter f = (TownFilter) filter;
            return list(f.getSelectedStub().getId());
        }
    }

	public static final class PersonSearchFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof PersonSearchFilter;
		}

		@Override
		public List<String> whereClause(ObjectFilter filter, StringBuilder clause) {
            clause.append("upper(ci.lastName || ' ' || ci.firstName || ' ' || ci.middleName) like upper(?))");
			PersonSearchFilter f = (PersonSearchFilter) filter;
			return list("%" + f.getSearchString() + "%");
		}
	}

}
