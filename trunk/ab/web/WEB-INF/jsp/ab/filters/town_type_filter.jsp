<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.town_type"/>&nbsp;
<select name="townTypeFilter.selectedId"><s:iterator value="townTypeFilter.names">
	<option value="<s:property value="translatable.id"/>"<s:if test="%{translatable.id == townFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>