<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="%{organizationFilter.isReadOnly()}">
	<s:hidden name="organizationFilter.selectedId" />
	<s:property value="getTranslation(organizationFilter.selected.names).name"/>
</s:if><s:else><select name="organizationFilter.selectedId" class="form-select">
	<s:if test="organizationFilter.allowEmpty"><option value="-1"><s:text name="eirc.organization" /></option></s:if>
	<s:iterator value="organizationFilter.organizations">
	<option value="<s:property value="id"/>"<s:if test="%{id == organizationFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(names).name"/></option></s:iterator>
</select></s:else>
