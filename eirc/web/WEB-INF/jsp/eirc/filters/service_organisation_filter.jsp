<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="serviceOrganisationFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.service_organisation"/></option><s:iterator value="serviceOrganisationFilter.organisations">
	<option value="<s:property value="id"/>"<s:if test="%{id == serviceOrganisationFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(organisation.names).name"/></option></s:iterator>
</select>
