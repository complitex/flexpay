<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.street"/>&nbsp;
<select name="streetFilter.selectedId" onchange="this.form.submit()"><s:iterator value="streetFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == streetFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>