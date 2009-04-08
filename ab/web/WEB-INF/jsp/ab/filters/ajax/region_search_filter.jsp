<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="regionFilter.showSearchString && regionFilter.searchString != null">
	<s:set name="regionFilter.field.value" value="regionFilter.searchString" />
</s:if>

<input type="hidden" id="selected_region_id" name="regionFilter.selectedId" value="<s:text name="%{userPreferences.regionFilterValue}" />" />
<input type="text" class="form-search" id="region_filter"
	   name="regionFilter.searchString"
	   value="" />
