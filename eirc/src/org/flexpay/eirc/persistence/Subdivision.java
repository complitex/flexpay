package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.Collections;

public class Subdivision extends DomainObjectWithStatus {

	private Organisation headOrganisation;
	private Subdivision parentSubdivision;
	private Organisation juridicalPerson;

	private String realAddress;

	private Set<SubdivisionName> names = Collections.emptySet();
	private Set<SubdivisionDescription> descriptions = Collections.emptySet();
	private Set<Subdivision> childSubdivisions = Collections.emptySet();


	/**
	 * Constructs a new DomainObject.
	 */
	public Subdivision() {
	}

	public Subdivision(@NotNull Long id) {
		super(id);
	}

	public Subdivision(@NotNull Stub<Subdivision> stub) {
		super(stub.getId());
	}

	@NotNull
	public Organisation getHeadOrganisation() {
		return headOrganisation;
	}

	public void setHeadOrganisation(@NotNull Organisation headOrganisation) {
		this.headOrganisation = headOrganisation;
	}

	@Nullable
	public Subdivision getParentSubdivision() {
		return parentSubdivision;
	}

	public void setParentSubdivision(@Nullable Subdivision parentSubdivision) {
		this.parentSubdivision = parentSubdivision;
	}

	@Nullable
	public Organisation getJuridicalPerson() {
		return juridicalPerson;
	}

	public void setJuridicalPerson(@Nullable Organisation juridicalPerson) {
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

	public void setRealAddress(String realAddress) {
		this.realAddress = realAddress;
	}
}
