<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="senderOrganisationFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.sender"/></option><s:iterator value="senderOrganisationFilter.organisations">
	<option value="<s:property value="id"/>"<s:if test="%{id == senderOrganisationFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>
