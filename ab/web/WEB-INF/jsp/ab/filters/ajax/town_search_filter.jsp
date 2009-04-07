<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="townFilter.showSearchString && townFilter.searchString != null">
	<s:set name="townFilter.field.value" value="townFilter.searchString" />
</s:if>

<input type="hidden" id="selected_town_id" name="townFilter.selectedId" value="<s:text name="%{userPreferences.townFilterValue}" />" />
<input type="text" class="form-search" id="town_filter"
	   name="townFilter.searchString"
	   value="<s:text name="%{townFilter.field.value}" />" />
