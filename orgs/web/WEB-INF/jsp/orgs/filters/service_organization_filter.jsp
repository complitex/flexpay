<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<select name="serviceOrganizationFilter.selectedId" id="serviceOrganizationFilterSelectedId" class="form-select">
	<option value="-1"><s:text name="orgs.service_organization" /></option><s:iterator value="serviceOrganizationFilter.organizations">
	<option value="<s:property value="id" />"<s:if test="id == serviceOrganizationFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(organization.names)" /></option></s:iterator>
</select>
