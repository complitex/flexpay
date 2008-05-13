<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="recipientOrganisationFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.recipient"/></option><s:iterator value="recipientOrganisationFilter.organisations">
	<option value="<s:property value="id"/>"<s:if test="%{id == recipientOrganisationFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>
