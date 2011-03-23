package org.flexpay.payments.dao.impl;

import org.flexpay.common.dao.FilterHandler;
import org.flexpay.common.persistence.filter.*;
import org.flexpay.orgs.persistence.filters.RecipientOrganizationFilter;
import org.flexpay.orgs.persistence.filters.SenderOrganizationFilter;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public final class FilterHandlers {

	private FilterHandlers() {
	}

	@NotNull
	public static List<FilterHandler> registryFilterHandlers() {
		return list(
				new SenderOrganizationFilterHandler(),
				new RecipientOrganizationFilterHandler(),
                new ServiceProviderFilterHandler(),
				new RegistryTypeFilterHandler(),
				new FPModuleFilterHandler(),
				new BeginDateFilterHandler(),
				new EndDateFilterHandler()
		);
	}

	public static final class SenderOrganizationFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof SenderOrganizationFilter;
		}

		@Override
		public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
			clause.append("sender.id=?");
			SenderOrganizationFilter f = (SenderOrganizationFilter) filter;
			return list(f.getSelectedStub().getId());
		}
	}

	public static final class RecipientOrganizationFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof RecipientOrganizationFilter;
		}

		@Override
		public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
			clause.append("recipient.id=?");
			RecipientOrganizationFilter f = (RecipientOrganizationFilter) filter;
			return list(f.getSelectedStub().getId());
		}
	}

	public static final class RegistryTypeFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof RegistryTypeFilter;
		}

		@Override
		public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
            clause.append("r.registryType.id=?");
			RegistryTypeFilter f = (RegistryTypeFilter) filter;
			return list(f.getSelectedStub().getId());
		}
	}

    public static final class ServiceProviderFilterHandler implements FilterHandler {

        @Override
        public boolean supports(ObjectFilter filter) {
            return filter instanceof ServiceProviderFilter;
        }

        @Override
        public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
            clause.append("serviceProvider.id=?");
            ServiceProviderFilter f = (ServiceProviderFilter) filter;
            return list(f.getSelectedStub().getId());
        }
    }

	public static final class FPModuleFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof FPModuleFilter;
		}

		@Override
		public List<Long> whereClause(ObjectFilter filter, StringBuilder clause) {
			clause.append("r.module.id=?");
			FPModuleFilter f = (FPModuleFilter) filter;
			return list(f.getSelectedStub().getId());
		}
	}

	public static final class BeginDateFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof BeginDateFilter;
		}

		@Override
		public List<Date> whereClause(ObjectFilter filter, StringBuilder clause) {
			clause.append("r.creationDate >= ?");
			BeginDateFilter f = (BeginDateFilter) filter;
			return list(f.getDate());
		}
	}

	public static final class EndDateFilterHandler implements FilterHandler {

		@Override
		public boolean supports(ObjectFilter filter) {
			return filter instanceof EndDateFilter;
		}

		@Override
		public List<Date> whereClause(ObjectFilter filter, StringBuilder clause) {
			clause.append("r.creationDate <= ?");
			EndDateFilter f = (EndDateFilter) filter;
			return list(f.getDate());
		}
	}
}
