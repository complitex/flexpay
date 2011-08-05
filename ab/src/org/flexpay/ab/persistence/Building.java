package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.EsbXmlSyncObject;
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
public class Building extends EsbXmlSyncObject {

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

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <building>\n");

        if (ACTION_INSERT.equals(action)) {

            BuildingAddress address = getDefaultBuildings();

            builder.append("        <districtId>").append(district.getId()).append("</districtId>\n").
                    append("        <buildingAddress>\n").
                    append("            <streetId>").append(address.getStreet().getId()).append("</streetId>\n").
                    append("            <attributes>\n");
            for (AddressAttribute attr : address.getBuildingAttributes()) {
                builder.append("                <org.flexpay.mule.request.MuleAddressAttribute>\n").
                        append("                    <id>").append(attr.getBuildingAttributeType().getId()).append("</id>\n").
                        append("                    <value>").append(attr.getValue()).append("</value>\n").
                        append("                </org.flexpay.mule.request.MuleAddressAttribute>\n");
            }
            builder.append("            </attributes>\n").
                    append("        </buildingAddress>\n");
        } else if (ACTION_DELETE.equals(action)) {
            builder.append("        <ids>\n");
            for (Long id : ids) {
                builder.append("            <long>").append(id).append("</long>\n");
            }
            builder.append("        </ids>\n");
        }

        builder.append("    </building>\n");

        return builder.toString();
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

	public Set<BuildingStatus> getBuildingStatuses() {
		return this.buildingStatuses;
	}

	public void setBuildingStatuses(Set<BuildingStatus> buildingStatuses) {
		this.buildingStatuses = buildingStatuses;
	}

	public Set<BuildingAddress> getBuildingses() {
		return buildingses;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setBuildingses(Set<BuildingAddress> buildingses) {
		this.buildingses = buildingses;
	}

	public Set<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(Set<Apartment> apartments) {
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
