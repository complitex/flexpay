
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="senderOrganizationFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.sender"/></option><s:iterator value="senderOrganizationFilter.organizations">
	<option value="<s:property value="id"/>"<s:if test="%{id == senderOrganizationFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(names).name"/></option></s:iterator>
</select>
