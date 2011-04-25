<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="organizationFilter.isReadOnly()">
	<s:hidden name="organizationFilter.selectedId" />
	<s:property value="getTranslationName(organizationFilter.selected.names)" />
</s:if><s:else>
    <select name="organizationFilter.selectedId" class="form-select">
        <s:if test="organizationFilter.allowEmpty"><option value="-1"><s:text name="orgs.organization" /></option></s:if>
        <s:iterator value="organizationFilter.organizations">
        <option value="<s:property value="id" />"<s:if test="id == organizationFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(names)" /></option></s:iterator>
    </select>
</s:else>
