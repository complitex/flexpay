package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Building
 */
public class Building extends DomainObjectWithStatus {

	private District district;
	private Set<BuildingStatus> buildingStatuses = Collections.emptySet();
	private Set<BuildingAddress> buildingses = Collections.emptySet();
	private Set<Apartment> apartments = Collections.emptySet();

	public Building() {
	}

	public Building(Long id) {
		super(id);
	}

	public Building(Stub<Building> stub) {
		super(stub.getId());
	}

	public static Building newInstance() {
		return new Building();
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(@Nullable District district) {
		this.district = district;
	}

	public Stub<District> getDistrictStub() {
		return stub(district);
	}

	@NotNull
	public Set<BuildingStatus> getBuildingStatuses() {
		return this.buildingStatuses;
	}

	public void setBuildingStatuses(@NotNull Set<BuildingStatus> buildingStatuses) {
		this.buildingStatuses = buildingStatuses;
	}

	@NotNull
	public Set<BuildingAddress> getBuildingses() {
		return buildingses;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setBuildingses(@NotNull Set<BuildingAddress> buildingses) {
		this.buildingses = buildingses;
	}

	@NotNull
	public Set<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(@NotNull Set<Apartment> apartments) {
		this.apartments = apartments;
	}

	public void addAddress(@NotNull BuildingAddress buildingAddress) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (buildingses == Collections.EMPTY_SET) {
			buildingses = set();
		}

		buildingAddress.setBuilding(this);
		buildingses.add(buildingAddress);
	}

	@Nullable
	public BuildingAddress getDefaultBuildings() {
		for (BuildingAddress buildingAddress : buildingses) {
			if (buildingAddress.isPrimary()) {
				return buildingAddress;
			}
		}

		return buildingses.isEmpty() ? null : buildingses.iterator().next();
	}

	@SuppressWarnings ({"CollectionsFieldAccessReplaceableByMethodCall"})
	public void addAll(Collection<BuildingAddress> buildingses) {
		if (this.buildingses == Collections.EMPTY_SET) {
			this.buildingses = set();
		}

		this.buildingses.addAll(buildingses);
	}

	/**
	 * Find all streets building has address on
	 *
	 * @return set of streets
	 */
	@NotNull
	public Set<Street> getStreets() {

		Set<Street> streets = set();
		for (BuildingAddress address : buildingses) {
			streets.add(address.getStreet());
		}

		return streets;
	}

	/**
	 * Find building address on a street
	 *
	 * @param street Street to get address on
	 * @return BuildingAddress if exists, or <code>null</code> otherwise
	 */
	@Nullable
	public BuildingAddress getAddressOnStreet(@NotNull Street street) {

		return getAddressOnStreet(stub(street));
	}

	/**
	 * Find building address on a street
	 *
	 * @param street Street to get address on
	 * @return BuildingAddress if exists, or <code>null</code> otherwise
	 */
	@Nullable
	public BuildingAddress getAddressOnStreet(@NotNull Stub<Street> street) {

		for (BuildingAddress address : getBuildingses()) {
			if (street.sameId(address.getStreet())) {
				return address;
			}
		}

		return null;
	}

	@Nullable
	public BuildingAddress getAddress(Stub<BuildingAddress> stub) {

		for (BuildingAddress address : getBuildingses()) {
			if (stub.sameId(address)) {
				return address;
			}
		}

		return null;
	}

	/**
	 * Set primary status of an address of a building a stub references to
	 * @param stub Address stub
	 */
	public void setPrimaryAddress(Stub<BuildingAddress> stub) {

		for (BuildingAddress address : getBuildingses()) {
			address.setPrimaryStatus(stub.sameId(address));
		}
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("status", status).
                append("district", district).
                append("buildingses", buildingses).
                toString();
    }
}
