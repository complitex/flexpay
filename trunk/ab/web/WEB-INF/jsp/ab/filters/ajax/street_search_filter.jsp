<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="streetFilter.showSearchString && streetFilter.searchString != null">
	<s:set name="streetFilter.field.value" value="streetFilter.searchString" />
</s:if>

<input type="hidden" id="selected_street_id" name="streetFilter.selectedId" value="<s:text name="%{userPreferences.streetFilterValue}" />" />
<input type="text" class="form-search" id="street_filter"
	   name="streetFilter.searchString"
	   value="<s:text name="%{streetFilter.field.value}" />" />
