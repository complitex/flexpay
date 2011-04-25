<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="subdivisionFilter.isReadOnly()">
	<s:hidden name="subdivisionFilter.selectedId" />
	<s:property value="getTranslationName(subdivisionFilter.selected.names)" />
</s:if><s:else>
    <select name="subdivisionFilter.selectedId" class="form-select">
        <s:if test="subdivisionFilter.allowEmpty"><option value="-1"><s:text name="orgs.subdivision" /></option></s:if>
        <s:iterator value="subdivisionFilter.subdivisions">
        <option value="<s:property value="id" />"<s:if test="id == subdivisionFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(names)" /></option></s:iterator>
    </select>
</s:else>
