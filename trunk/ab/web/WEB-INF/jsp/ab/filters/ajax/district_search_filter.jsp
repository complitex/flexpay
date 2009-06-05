<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="districtFilter.showSearchString && districtFilter.searchString != null">
	<s:set name="districtFilter.field.value" value="districtFilter.searchString" />
</s:if>

<input type="hidden" id="selected_district_id" name="districtFilter.selectedId" value="<s:text name="%{userPreferences.districtFilterValue}" />" />
<input type="text" class="form-search" id="district_filter" name="districtFilter.searchString" value="" />
