<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<select name="recordStatusFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="payments.registry.record.status" /></option><s:iterator value="recordStatusFilter.recordStatuses">
	<option value="<s:property value="id" />"<s:if test="id == recordStatusFilter.selectedId"> selected</s:if>><s:text name="%{i18nName}" /></option></s:iterator>
</select>
