<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="countryFilter.showSearchString && countryFilter.searchString != null">
	<s:set name="countryFilter.field.value" value="countryFilter.searchString" />
</s:if>

<input type="hidden" id="selected_country_id" name="countryFilter.selectedId" value="<s:text name="%{userPreferences.countryFilterValue}" />" />
<input type="text" class="form-search" id="country_filter"
	   name="countryFilter.searchString"
	   value="" />
