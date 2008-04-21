<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.region"/>&nbsp;
<select name="regionFilter.selectedId" onchange="this.form.submit()"><s:iterator value="regionFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == regionFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>