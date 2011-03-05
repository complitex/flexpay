<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<select name="townTypeFilter.selectedId" class="form-select"><s:iterator value="townTypeFilter.names">
	<option value="<s:property value="translatable.id" />"<s:if test="translatable.id == townTypeFilter.selectedId"> selected</s:if>><s:property value="name" /></option></s:iterator>
</select>
