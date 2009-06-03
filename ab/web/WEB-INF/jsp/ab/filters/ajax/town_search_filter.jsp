<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="townFilter.showSearchString && townFilter.searchString != null">
	<s:set name="townFilter.field.value" value="townFilter.searchString" />
</s:if>

<s:if test="%{townFilter.needFilter()}">
	<s:set name="townId" value="%{townFilter.selectedId}" />
</s:if>
<s:else>
	<s:set name="townId" value="%{userPreferences.townFilterValue}" />
</s:else>
<s:hidden id="selected_town_id" name="townFilter.selectedId" value="%{#townId}" />
<input type="text" class="form-search" id="town_filter" name="townFilter.searchString" value="" />
