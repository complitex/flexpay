package org.flexpay.orgs.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class Subdivision extends DomainObjectWithStatus {

	private Organization headOrganization;
	private Subdivision parentSubdivision;
	private Organization juridicalPerson;

	private String realAddress;

	private Set<SubdivisionName> names = set();
	private Set<SubdivisionDescription> descriptions = set();
	private Set<Subdivision> childSubdivisions = set();

	// tree path is a parents path devided with dots, like .1.35.24
	private String treePath = "";

	public Subdivision() {
	}

	public Subdivision(@NotNull Long id) {
		super(id);
	}

	public Subdivision(@NotNull Stub<Subdivision> stub) {
		super(stub.getId());
	}

	public Organization getHeadOrganization() {
		return headOrganization;
	}

	public void setHeadOrganization(@NotNull Organization headOrganization) {
		this.headOrganization = headOrganization;
	}

	@Nullable
	public Subdivision getParentSubdivision() {
		return parentSubdivision;
	}

	public void setParentSubdivision(@Nullable Subdivision parentSubdivision) {
		this.parentSubdivision = parentSubdivision;
	}

	@Nullable
	public Organization getJuridicalPerson() {
		return juridicalPerson;
	}

	public void setJuridicalPerson(@Nullable Organization juridicalPerson) {
		this.juridicalPerson = juridicalPerson;
	}

	@NotNull
	public Set<SubdivisionName> getNames() {
		return names;
	}

	public void setNames(@NotNull Set<SubdivisionName> names) {
		this.names = names;
	}

	@NotNull
	public Set<SubdivisionDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(@NotNull Set<SubdivisionDescription> descriptions) {
		this.descriptions = descriptions;
	}

	@NotNull
	public Set<Subdivision> getChildSubdivisions() {
		return childSubdivisions;
	}

	public void setChildSubdivisions(@NotNull Set<Subdivision> childSubdivisions) {
		this.childSubdivisions = childSubdivisions;
	}

	public String getRealAddress() {
		return realAddress;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public void setRealAddress(String realAddress) {
		this.realAddress = realAddress;
	}

	public boolean hasHeadOrganization() {
		return headOrganization != null;
	}

	public boolean hasNoHeadOrganization() {
		return !hasHeadOrganization();
	}

	public boolean hasJuridicalPerson() {
		return juridicalPerson != null;
	}

	/**
	 * Get subdivision level in a hierarchy
	 *
	 * @return 0 for top level subdivisions, 1 - for sussubdivisions, etc
	 */
	public int getLevel() {
		int n = 0;
		Subdivision parent = getParentSubdivision();
		while (parent != null) {
			parent = parent.getParentSubdivision();
			++n;
		}

		return n;
	}

	/**
	 * Get a string that is repeted {@link #getLevel()} times
	 * @param str String to repeat
	 * @return String
	 */
	public String repeatLevelTimes(String str) {
		return StringUtils.repeat(str, getLevel());
	}

	public void setDescription(SubdivisionDescription description) {
		descriptions = TranslationUtil.setTranslation(descriptions, this, description);
	}

	public void setName(SubdivisionName name) {
		names = TranslationUtil.setTranslation(names, this, name);
	}

	/**
	 * Get name translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public SubdivisionName getNameTranslation(@NotNull Language lang) {

		for (SubdivisionName translation : getNames()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	/**
	 * Get description translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public SubdivisionDescription getDescriptionTranslation(@NotNull Language lang) {

		for (SubdivisionDescription translation : getDescriptions()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("level", getLevel()).
				append("realAddress", realAddress).
				append("treePath", treePath).
				toString();
	}

}
