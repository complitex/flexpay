<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<select name="parentServiceFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="payments.service.parent_service" /></option><s:iterator value="parentServiceFilter.services">
	<option value="<s:property value="id" />"<s:if test="id == parentServiceFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(serviceType.typeNames)" /> (<s:property value="getTranslationName(serviceProvider.organization.names)" />)</option></s:iterator>
</select>
