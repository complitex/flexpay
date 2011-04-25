<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<select name="senderOrganizationFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="orgs.sender" /></option><s:iterator value="senderOrganizationFilter.organizations">
	<option value="<s:property value="id" />"<s:if test="id == senderOrganizationFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(names)" /></option></s:iterator>
</select>
