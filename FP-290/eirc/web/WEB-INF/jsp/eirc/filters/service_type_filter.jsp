<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="serviceTypeFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.service_type"/><s:iterator value="serviceTypeFilter.serviceTypes">
	<option value="<s:property value="id"/>"<s:if test="%{id == serviceTypeFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(typeNames).name"/></option></s:iterator>
</select>
