<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<select name="districtFilter.selectedId" class="form-select"><s:iterator value="districtFilter.names">
	<option value="<s:property value="translatable.id" />"<s:if test="translatable.id == districtFilter.selectedId"> selected</s:if>><s:property value="name" /></option></s:iterator>
</select>
