<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="buildingsFilter.showSearchString && buildingsFilter.searchString != null">
	<s:set name="buildingsFilter.field.value" value="buildingsFilter.searchString" />
</s:if>

<s:if test="%{buildingsFilter != null && buildingsFilter.needFilter()}">
	<s:set name="buildingId" value="%{buildingsFilter.selectedId}" />
</s:if>
<s:else>
	<s:set name="buildingId" value="%{userPreferences.buildingFilterValue}" />
</s:else>
<s:hidden id="selected_building_id" name="buildingsFilter.selectedId" value="%{#buildingId}" />
<input type="text" class="form-search" id="building_filter" name="buildingsFilter.searchString" value="" />
