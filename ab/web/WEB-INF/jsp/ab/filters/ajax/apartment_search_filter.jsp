<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="apartmentFilter.showSearchString && apartmentFilter.searchString != null">
	<s:set name="apartmentFilter.field.value" value="apartmentFilter.searchString" />
</s:if>

<input type="hidden" id="selected_apartment_id" name="apartmentFilter.selectedId" value="<s:text name="%{userPreferences.apartmentFilterValue}" />" />
<input type="text" class="form-search" id="apartment_filter"
	   name="apartmentFilter.searchString"
	   value="" />
