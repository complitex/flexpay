<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="serviceOrganizationFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.service_organization"/></option><s:iterator value="serviceOrganizationFilter.organizations">
	<option value="<s:property value="id"/>"<s:if test="%{id == serviceOrganizationFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(organization.names).name"/></option></s:iterator>
</select>
