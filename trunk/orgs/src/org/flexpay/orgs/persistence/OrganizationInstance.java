package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

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
	private Set<D> descriptions = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	protected OrganizationInstance() {
	}

	protected OrganizationInstance(@NotNull Long id) {
		super(id);
	}

	protected OrganizationInstance(@NotNull Stub<OrganizationInstance<D, T>> stub) {
		super(stub.getId());
	}

	@Nullable
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


	public String getName(@NotNull Locale locale) {
		return TranslationUtil.getTranslation(organization.getNames(), locale).getName();
	}

	public String getName() {
		return getName(ApplicationConfig.getDefaultLocale());
	}
}
