<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<select id="serviceProviderFilter" name="serviceProviderFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.service_provider" /></option><s:iterator value="serviceProviderFilter.instances">
	<option value="<s:property value="id" />"<s:if test="id == serviceProviderFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(organization.names)" /></option></s:iterator>
</select>
