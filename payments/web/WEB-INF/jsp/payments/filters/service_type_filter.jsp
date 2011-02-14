<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<select name="serviceTypeFilter.selectedId" id="serviceTypeFilter" class="form-select"<s:if test="serviceTypeFilter.disabled"> disabled="disabled"</s:if>>
	<option value="-1"><s:text name="payments.service_type" /><s:iterator value="serviceTypeFilter.serviceTypes">
	<option value="<s:property value="id" />"<s:if test="id == serviceTypeFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(typeNames)" /></option></s:iterator>
</select>
