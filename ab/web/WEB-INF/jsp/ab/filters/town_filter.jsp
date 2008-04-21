<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.town"/>&nbsp;
<select name="townFilter.selectedId" onchange="this.form.submit()"><s:iterator value="townFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == townFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>