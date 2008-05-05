<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="importErrorTypeFilter.selectedType"><s:iterator value="importErrorTypeFilter.errorTypes">
	<option value="<s:property value="key"/>"<s:if test="%{key == importErrorTypeFilter.selectedType}"> selected</s:if>><s:text name="%{value}"/></option></s:iterator>
</select>
