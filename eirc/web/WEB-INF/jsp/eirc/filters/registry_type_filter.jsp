<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="registryTypeFilter.selectedId">
	<option value="-1"><s:text name="eirc.registry_type"/></option><s:iterator value="registryTypeFilter.registryTypes">
	<option value="<s:property value="id"/>"<s:if test="%{id == registryTypeFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>
