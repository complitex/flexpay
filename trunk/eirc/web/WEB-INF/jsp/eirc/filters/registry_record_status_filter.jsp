<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="recordStatusFilter.selectedStatus"><s:iterator value="recordStatusFilter.statusTypes">
	<option value="<s:property value="key"/>"<s:if test="%{key == recordStatusFilter.selectedStatus}"> selected</s:if>><s:text name="%{value}"/></option></s:iterator>
</select>
