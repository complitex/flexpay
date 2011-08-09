package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Particular Juridical person instance, may be only one for single organisation
 *
 * @param <T> Concrete type of instance
 */
public abstract class OrganizationInstance<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>>
		extends DomainObjectWithStatus {

	private Organization organization;
	private Set<D> descriptions = set();

	/**
	 * Constructs a new DomainObject.
	 */
	protected OrganizationInstance() {
	}

	protected OrganizationInstance(@NotNull Long id) {
		super(id);
	}

	public Organization getOrganization() {
		return organization;
	}

	public Stub<Organization> getOrganizationStub() {
		return stub(organization);
	}

	public void setOrganization(@NotNull Organization organization) {
		this.organization = organization;
	}

	@NotNull
	public Set<D> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<D> descriptions) {
		this.descriptions = descriptions;
	}

	public void setDescription(D description) {
		descriptions = TranslationUtil.setTranslation(descriptions, this, description);
	}

	/**
	 * Get description translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public D getDescription(@NotNull Language lang) {

		for (D translation : getDescriptions()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	public String getName(@NotNull Locale locale) {
		return TranslationUtil.getTranslation(organization.getNames(), locale).getName();
	}

	public String getName() {
		return getName(ApplicationConfig.getDefaultLocale());
	}
}
