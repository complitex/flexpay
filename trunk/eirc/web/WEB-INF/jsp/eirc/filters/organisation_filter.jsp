<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="%{organisationFilter.isReadOnly()}">
	<s:hidden name="organisationFilter.selectedId" />
	<s:property value="getTranslation(organisationFilter.selected.names).name"/>
</s:if><s:else><select name="organisationFilter.selectedId" class="form-select">
	<s:iterator value="organisationFilter.organisations">
	<option value="<s:property value="id"/>"<s:if test="%{id == organisationFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(names).name"/></option></s:iterator>
</select></s:else>
