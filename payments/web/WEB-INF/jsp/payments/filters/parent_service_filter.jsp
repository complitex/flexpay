
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="parentServiceFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.service.parent_service"/></option><s:iterator value="parentServiceFilter.services">
	<option value="<s:property value="id"/>"<s:if test="%{id == parentServiceFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(serviceType.typeNames).name"/> (<s:property value="getTranslation(serviceProvider.organization.names).name"/>)</option></s:iterator>
</select>
