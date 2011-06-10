package org.flexpay.tc.actions.buildingattributes;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.bti.persistence.building.*;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.bti.service.BuildingAttributeGroupService;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuildingAttributesEditBlockAction extends FPActionSupport {

	private BuildingAddress building = new BuildingAddress();

	private Date attributeDate = DateUtil.now();

	private Map<Long, String> attrs = CollectionUtils.map();
	private Map<Long, Set<BuildingAttributeType>> typesMap = CollectionUtils.map();
	private Set<BuildingAttributeGroup> groups = CollectionUtils.set();

	private BtiBuildingService btiBuildingService;
	private BuildingAttributeTypeService buildingAttributeTypeService;
	private BuildingAttributeGroupService buildingAttributeGroupService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		BtiBuilding btiBuilding = btiBuildingService.readWithAttributesByAddress(stub(building));
		if (btiBuilding == null) {
			throw new FlexPayException("No bti building with id " + building.getId() + " can be retrieved");
		}

		List<BuildingAttributeType> attributeTypes = buildingAttributeTypeService.readFullAll();

		Set<Long> groupIds = CollectionUtils.set();

		for (BuildingAttributeType type : attributeTypes) {
			BuildingAttribute attribute = btiBuilding.getAttributeForDate(type, attributeDate);
			String value = attribute != null ? String.valueOf(attribute.value()) : "";
			attrs.put(type.getId(), value);
			log.debug("Added attribute: {}, {}", type.getId(), value);

			Long groupId = type.getGroup().getId();
			groupIds.add(groupId);
			Set<BuildingAttributeType> types = typesMap.get(groupId);
			if (types == null) {
				types = CollectionUtils.set();
			}
			types.add(type);
			typesMap.put(groupId, types);
		}

		groups.addAll(buildingAttributeGroupService.readFullGroups(groupIds));

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public boolean isBuildingAttributeTypeSimple(BuildingAttributeType type) {
		return type instanceof BuildingAttributeTypeSimple;
	}

	public boolean isBuildingAttributeTypeEnum(BuildingAttributeType type) {
		return type instanceof BuildingAttributeTypeEnum;
	}

	public BuildingAddress getBuilding() {
		return building;
	}

	public void setBuilding(BuildingAddress building) {
		this.building = building;
	}

	public String getAttributeDate() {
		return format(attributeDate);
	}

	public void setAttributeDate(String attributeDate) {
		this.attributeDate = DateUtil.parseBeginDate(attributeDate);
	}

	public Set<BuildingAttributeGroup> getGroups() {
		return groups;
	}

	public Map<Long, String> getAttrs() {
		return attrs;
	}

	public Map<Long, Set<BuildingAttributeType>> getTypesMap() {
		return typesMap;
	}

	@Required
	public void setBtiBuildingService(BtiBuildingService btiBuildingService) {
		this.btiBuildingService = btiBuildingService;
	}

	@Required
	public void setBuildingAttributeTypeService(BuildingAttributeTypeService buildingAttributeTypeService) {
		this.buildingAttributeTypeService = buildingAttributeTypeService;
	}

	@Required
	public void setBuildingAttributeGroupService(BuildingAttributeGroupService buildingAttributeGroupService) {
		this.buildingAttributeGroupService = buildingAttributeGroupService;
	}

}
