<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="buildingsFilter.showSearchString && buildingsFilter.searchString != null">
	<s:set name="buildingsFilter.field.value" value="buildingsFilter.searchString" />
</s:if>

<input type="hidden" id="selected_building_id" name="buildingsFilter.selectedId" value="<s:text name="%{userPreferences.buildingFilterValue}" />" />
<input type="text" class="form-search" id="building_filter"
	   name="buildingsFilter.searchString"
	   value="" />
