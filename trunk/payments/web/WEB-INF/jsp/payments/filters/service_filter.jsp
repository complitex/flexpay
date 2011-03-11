<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<select name="serviceFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.service" /></option><s:iterator value="serviceFilter.services">
	<option value="<s:property value="id" />"<s:if test="id == serviceFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(serviceType.typeNames)" /> (<s:property value="getTranslationName(serviceProvider.organization.names)" />)</option></s:iterator>
</select>
