<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="recipientOrganizationFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.recipient"/></option><s:iterator value="recipientOrganizationFilter.organizations">
	<option value="<s:property value="id"/>"<s:if test="%{id == recipientOrganizationFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(names).name"/></option></s:iterator>
</select>
