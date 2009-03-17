package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

	public void addBuildings(@NotNull BuildingAddress buildingAddress) {
		if (Collections.emptySet().equals(buildingses)) {
			buildingses = new HashSet<BuildingAddress>();
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
			this.buildingses = CollectionUtils.set();
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

		Set<Street> streets = CollectionUtils.set();
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

		for (BuildingAddress address : buildingses) {
			if (street.equals(address.getStreet())) {
				return address;
			}
		}

		return null;
	}
}
